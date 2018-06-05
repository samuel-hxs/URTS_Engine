package debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import main.Fonts;
import menu.AdvancedTextEnterField;
import term.Terminal;
import term.TerminalExeption;

public class TerminalMenu{

	private term.VisualisedTerminal vt;
	private AdvancedTextEnterField teb;
	private Terminal tc;
	private String lastSearch = "zzz";
	
	private String toolTip;
	private String[] preClicks;
	private boolean showClicks = true;
	private int select = -1;
	
	private boolean activ = false;
	private Color egshellColor = new Color(236,223,162);
	
	private String[] lastExec;
	private int lastSelect = -1;
	
	public TerminalMenu(int x, int y, term.VisualisedTerminal v, Terminal tc) {
		vt = v;
		this.tc = tc;
		
		lastExec = new String[10];
		for (int i = 0; i < lastExec.length; i++) {
			lastExec[i] = "";
		}
		
		teb = new AdvancedTextEnterField(){
			@Override
			protected void specialKey(int id) {
				if(id == AdvancedTextEnterField.BUTTON_CTRL_SPACE){
					lastSearch = "zzz";
					tryAutoFill(select);
				}
				if(id == AdvancedTextEnterField.BUTTON_DOWN)
					chooseDown();
				if(id == AdvancedTextEnterField.BUTTON_UP)
					chooseUp();
				if(id == AdvancedTextEnterField.BUTTON_ENTER){
					if(!tryAutoFill(select))
						exec();
				}
			}

			@Override
			protected boolean isSpecialChar(char c) {
				if(c != 0)
					lastSelect = -1;
				if(c == ' ' || c == '.')
					return tryAutoFill(select);
				return false;
			}
		};
	}

	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		g.translate(-15, -30);
		vt.paintYou(g, 15, 30);
		gr.setColor(Color.black);
		gr.fillRect(15, 30+vt.ySize, vt.xSize, 20);
		gr.setColor(Color.white);
		gr.setFont(Fonts.font14);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gr.drawString(teb.text, 30, vt.ySize+45);
		if(System.currentTimeMillis()%1000 < 500 && activ){
			int u = gr.getFontMetrics().stringWidth(teb.text.substring(0, teb.tebpos));
			g.drawRect(30+u, vt.ySize+33, 1, 15);
		}
		int xp = 0;
		int yp = teb.text.lastIndexOf(".");
		if(yp < 0) yp = 0;
		else yp = gr.getFontMetrics().stringWidth(teb.text.substring(0,yp+1));
		if(toolTip != null){
			xp = 14;
			g.setColor(egshellColor);
			g.fillRect(yp+27, vt.ySize+50, 100, 14);
			g.setColor(Color.darkGray);
			gr.drawString(toolTip, yp+30, vt.ySize+60);
			g.drawRect(yp+27, vt.ySize+50, 100, 14);
		}
		if(preClicks != null && showClicks){
			for (int i = 0; i < preClicks.length; i++) {
				g.setColor(egshellColor);
				if(i == select)
					g.setColor(Color.cyan);
				g.fillRect(yp+27, vt.ySize+50+i*12+xp, 100, 12);
				g.setColor(Color.darkGray);
				gr.drawString(preClicks[i], yp+30, vt.ySize+59+i*12+xp);
			}
			if(preClicks.length > 0)
				g.drawRect(yp+27, vt.ySize+50+xp, 100, 12*preClicks.length);
		}
		g.translate(15, 30);
	}
	
	public void leftClick(int x, int y) {
		boolean in = x>=0 && y>=0 && x<vt.xSize && y<vt.ySize+30;
		if(in != activ){
			if(in) setFocus();
			else deFocus();
		}
	}

	protected void uppdateIntern() {
		if(teb.tebpos > teb.text.length())
			return;
		String t = teb.text.substring(0, teb.tebpos);
		if(t.compareTo(lastSearch) == 0)
			return;
		
		lastSearch = t;
		
		preClicks = tc.getChoosing(t);
		if(preClicks == null)
			preClicks = new String[]{};
		if(select>=preClicks.length)
			select = preClicks.length-1;
		
		toolTip = tc.getToolTip(t);
	}
	
	public void setFocus(){
		activ = true;
		KeyListener.forwardKey = teb;
	}
	
	public void deFocus(){
		if(!activ)
			return;
		activ = false;
		KeyListener.forwardKey = null;
	}
	
	private boolean tryAutoFill(int p){
		if(p == -1){
			if(preClicks.length != 1)
				return false;
			p = 0;
		}
		String preText = teb.text.substring(0, teb.tebpos);
		if(p == 0 && preText.endsWith(preClicks[0])){
			return false;
		}
		int q = preText.lastIndexOf(".");
		q++;
		String newT = preText.substring(0, q);
		newT += preClicks[p];
		q = teb.tebpos;
		teb.tebpos += newT.length()-preText.length();
		newT += teb.text.substring(q);
		teb.text = newT;
		select = -1;
		return true;
	}
	
	public void chooseDown(){
		if(lastSelect>=0){
			lastSelect--;
			if(lastSelect<0)
				teb.text = "";
			else
				teb.text = lastExec[lastSelect];
			
			teb.tebpos = teb.text.length();
			return;
		}
		select++;
		if(select>=preClicks.length-1)
			select = preClicks.length-1; 
	}
	
	public void chooseUp(){
		if((teb.tebpos == 0 || lastSelect != -1)&&select<0){
			if(lastSelect == -1)
				lastSelect++;
			else if(lastExec[lastSelect].length()>0)
				lastSelect++;
			if(lastSelect>=lastExec.length)
				lastSelect=lastExec.length-1;
			teb.text = lastExec[lastSelect];
			
			teb.tebpos = teb.text.length();
			return;
		}
		select--;
		if(select<-1)
			select = -1;
	}
	
	private void exec(){
		if(teb.text.length()<=0)
			return;
		vt.println("*EXEC: "+teb.text, debug.Debug.PRICOM);
		try {
			tc.exec(teb.text, vt);
		} catch (TerminalExeption e) {
			vt.println("Terminal Exeption: "+e.getMessage(), debug.Debug.COMERR);
		}
		
		for (int i = lastExec.length-2; i >= 0; i--) {
			lastExec[i+1] = lastExec[i];
		}
		lastExec[0] = teb.text;
		lastSelect = -1;
		
		teb.tebpos = 0;
		teb.text = "";
	}

}
