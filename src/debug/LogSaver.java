package debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import term.TermColors;
import term.TermPrint;

public class LogSaver implements TermPrint{

	public final String logFilepath;
	private long lastTime;
	
	public static final boolean singleLog = false;
	
	public LogSaver(){
		if(singleLog){
			logFilepath = "log/0-CurrLOG.txt";
		}else{
			logFilepath = "log/"+
					new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new java.util.Date (System.currentTimeMillis()))+
					"-LOG.txt";
		}
		if(!new File("log/").exists())
			new File("log/").mkdir();
		
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath)); 
			writer.println("This is the Log of all Console Data: "+
					new java.text.SimpleDateFormat("dd.MM.yy").format(new java.util.Date (System.currentTimeMillis())));
			writer.println("-------------------------------------------");
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
	}

	@Override
	public void print(String s) {
		print(s, TermColors.TEXT);
	}

	@Override
	public void println(String s) {
		println(s, 0);
	}

	@Override
	public void print(String s, int color) {
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath, true)); 
			writer.print(s);
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
	}

	@Override
	public void println(String s, int color) {
		if(s.length()<=0)
			return;
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath, true));
			writer.println();
			char c = s.charAt(0);
			String time;
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
			}
			writer.print(time+getPreString(color)+" "+s);
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
	}
	
	private static String getPreString(int i){
		switch(i){
		case TermColors.ERROR: return "[ER]";
		case TermColors.SUBERR: return "[SE]";
		
		case TermColors.COM: return "[co]";
		case TermColors.SUBCOM: return "[sc]";
		case TermColors.COMERR: return "[CE]";
		case TermColors.PRICOM: return "[cp]";
		
		case TermColors.WARN: return "[WA]";
		case TermColors.SUBWARN: return "[SW]";
		
		case TermColors.MESSAGE: return "[--]";
		
		case TermColors.FATAL: return "[XX]";
		
		case TermColors.TEXT: return "[--]";
		
		case TermColors.REMOTE: return"[re]";
		}
		return "[??]";
	}
}
