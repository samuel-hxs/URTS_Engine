package debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import term.TermColors;
import term.TermPrint;

public class LogSaver extends Thread implements TermPrint{

	public final String logFilepath;
	public final String glLogFilepath;
	private long lastTime;
	
	public static final boolean singleLog = false;
	
	private static final String DIVIDER = "-------------------------------------------";
	
	private PerformanceMonitor p1, p2, p3;
	
	private PrintStream printStream;
	
	public LogSaver() {
		if(singleLog) {
			glLogFilepath = "log/0-CurrLOG_GL.txt";
			logFilepath = "log/0-CurrLOG.txt";
		} else {
			String date_formatted = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new java.util.Date (System.currentTimeMillis()));
			logFilepath = "log/" + date_formatted + "-LOG.txt";
			glLogFilepath = "log/" + date_formatted + "-LOG_GL.txt";
		}
		
		if(!new File("log/").exists()) {
			new File("log/").mkdir();
		}
		
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath)); 
			writer.println("This is the Log of all Console Data: " + new java.text.SimpleDateFormat("dd.MM.yy").format(new java.util.Date (System.currentTimeMillis())));
			writer.println(DIVIDER);
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
		
		Runtime.getRuntime().addShutdownHook(this);
	}

	@Override
	public void print(String s) {
		print(s, TermColors.TEXT);
	}

	@Override
	public void println(String s) {
		println(s, TermColors.TEXT);
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
		if(s.length()<=0) {
			return;
		}
		
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
	
	public void logError(Exception e){
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath, true)); 
			e.printStackTrace(writer);
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
	
	public void setShutdownData(PerformanceMonitor p1, PerformanceMonitor p2, PerformanceMonitor p3){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	@Override
	public void run() {
		PrintWriter writer = null; 
		try { 
			writer = new PrintWriter(new FileWriter(logFilepath, true));
			
			writer.println("");
			writer.println("");
			writer.println("Shutdown!");
			writer.println(DIVIDER);
			writer.println("");
			
			writePerformance(writer);
			
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		} finally { 
			if (writer != null){ 
				writer.flush(); 
				writer.close(); 
			} 
		}
	}
	
	public void writePerformance(PrintWriter writer){
		writer.println("------Operation-Time: Simpel");
		if(p1 != null){
			p1.print(writer);
		}
		writer.println("");
		writer.println("------Operation-Time: Complex");
		if(p2 != null){
			p2.print(writer);
		}
		writer.println("");
		writer.println("------CPU vs GPU - Time needed");
		if(p3 != null){
			p3.print(writer);
		}
	}

	public void openPrintStream() {		
		if(!new File("log/").exists()) {
			new File("log/").mkdir();
		}
		
		try {
			printStream = new PrintStream(glLogFilepath);
			printStream.println("This is the Log of all Console Data: " + new java.text.SimpleDateFormat("dd.MM.yy").format(new java.util.Date (System.currentTimeMillis())));
			printStream.println(DIVIDER);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally { 
			if (printStream != null) { 
				printStream.flush(); 
			} 
		}
	}

	public PrintStream getPrintStream() {
		if(printStream == null) {
			openPrintStream();
		}
		return printStream;
	}
	
	public void closePrintStream() {
		if (printStream != null){ 
			printStream.flush(); 
			printStream.close(); 
		} 
	}
}
