package term;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import main.Fonts;

public class VisualisedTerminal implements TermPrint{
	
	private long lastTime;
	
	private BufferedImage[] ima;
	private Graphics2D[] gr;
	private int current = 0;
	
	private int xPos;
	public int xSize;
	public int ySize;
	
	private String lastFront = "";
	
	private Font mono;
	
	public VisualisedTerminal() {
		setSize(370, 500);
		mono = new Font(Font.MONOSPACED, Font.PLAIN, 14);
	}
	
	public void setSize(int x, int y){
		ima = new BufferedImage[]{new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB),
				new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB)};
		xSize = x;
		ySize = y;
		
		gr = new Graphics2D[]{
				ima[0].createGraphics(), ima[1].createGraphics()
		};
		for (int i = 0; i < gr.length; i++) {
			gr[i].setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
	}

	@Override
	public synchronized void print(String s) {
		print(s, TermColors.TEXT);
	}

	@Override
	public synchronized void println(String s) {
		println(s, TermColors.TEXT);	
	}

	@Override
	public synchronized void print(String s, int color) {
		String[] st = s.split(Transmitter.HEADER);
		if(st.length>1){
			if(st[0].compareTo(lastFront) != 0)
				println(s, color);
			else
				drawString(st[1], color, false);
		}else{
			drawString(s, color, false);
		}
	}

	@Override
	public synchronized void println(String s, int color) {
		nextLine();
		String[] st = s.split(Transmitter.HEADER);
		String pre;
		String text;
		String time;
		if(st.length>1){
			text = st[1];
			pre = st[0];
		}else{
			text = s;
			pre = "";
		}
		boolean add = false;
		char c = text.charAt(0);
		if(c == '*' || c == '/' || c == '~'){
			lastTime = System.currentTimeMillis();
			time = "["+utility.TimeFormat.getTimeDay(lastTime, true)+"]";
		}else{
			String ti = "+"+(System.currentTimeMillis()-lastTime);
			String tii = "[";
			if(ti.length()+tii.length()<8)
				ti+=" ";
			while (ti.length()+tii.length()<9) {
				tii += " ";
			}
			time = tii+ti+"]";
			add = true;
		}
		if(lastFront.compareTo(pre) != 0){
			lastFront = pre;
			add = false;
		}
		drawPre(pre, time, add);
		drawString(text, color, add);
	}

	private void nextLine(){
		gr[(current+1)%2].drawImage(ima[current], 0, -14, null);
		gr[(current+1)%2].setColor(Color.black);
		gr[(current+1)%2].fillRect(0, ySize-14, xSize, 14);
		current++;
		if(current > 1)
			current = 0;
		xPos = 0;
	}
	
	private void drawPre(String p, String t, boolean gray){
		gr[current].setFont(Fonts.font14);
		if(gray)
			gr[current].setColor(Color.gray);
		else
			gr[current].setColor(Color.white);
		gr[current].drawString(p, 3, ySize-4);
		if(gray)
			gr[current].setColor(Color.white);
		gr[current].drawString(t, xSize-90, ySize-4);
	}
	
	private void drawString(String s, int c, boolean add){
		Color col = new Color(c);
		
		gr[0].setFont(Fonts.font14);
		gr[1].setFont(Fonts.font14);
		
		String[] sk = s.split(" ");
		sk[0] += " ";
		int[] sl = new int[sk.length];
		FontMetrics fm = gr[current].getFontMetrics();
		for (int i = 0; i < sl.length; i++) {
			sk[i] += " ";
			sl[i] = fm.stringWidth(sk[i]);
		}
		if(add)
			sk[0] = " "+sk[0];
		
		int frontlength = 8+fm.stringWidth(lastFront);
		
		for (int i = 0; i < sl.length; i++) {
			if(xPos+sl[i] > xSize-83-frontlength && xPos>0){
				nextLine();
				sk[i] = " "+sk[i];
			}
			if(xPos+sl[i] > xSize-83-frontlength){
				int u = 0;
				boolean doit = false;
				for (int j = 0; j < sk[i].length(); j++) {
					if(xPos+fm.stringWidth(sk[i].substring(u, j)) < xSize-83-frontlength)
						continue;
					if(doit){
						nextLine();
						xPos += 8;
					}
					doit = true;
					gr[current].setColor(col);
					gr[current].drawString(sk[i].substring(u, j), frontlength+xPos, ySize-4);
					u = j;
					xPos += fm.stringWidth(sk[i].substring(u, j));
				}
				nextLine();
				xPos += 8;
				gr[current].setColor(col);
				gr[current].drawString(sk[i].substring(u), frontlength+xPos, ySize-4);
				xPos += fm.stringWidth(sk[i].substring(u));
			}else{
				gr[current].setColor(col);
				gr[current].drawString(sk[i], frontlength+xPos, ySize-4);
				xPos += sl[i];
			}
		}
	}
	
	public synchronized void paintYou(Graphics g, int x, int y){
		g.drawImage(ima[current], x, y, null);
		g.setColor(Color.gray);
		g.drawRect(x, y, xSize, ySize);
	}
}
