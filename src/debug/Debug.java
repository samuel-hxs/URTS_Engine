package debug;

import term.TermColors;
import term.VisualisedTerminal;

public class Debug extends TermColors{
	
	private static VisualisedTerminal term;
	private static LogSaver log;
	
	public static void init(VisualisedTerminal vt){
		term = vt;
		log = new LogSaver();
	}
	
	public static void print(String s){
		term.print(s, TEXT);
		log.print(s, TEXT);
	}
	
	public static void print(String s, int c){
		term.print(s, c);
		log.print(s, c);
	}
	
	public static void println(String s){
		term.println(s);
		log.println(s);
	}
	
	public static void println(String s, int c){
		term.println(s, c);
		log.println(s, c);
	}
	
	public static void printException(Exception e){
		//TODO 
		log.logError(e);
		e.printStackTrace();
	}
	
	public static void z_setShutdownDebug(PerformanceMonitor p1, PerformanceMonitor p2, PerformanceMonitor p3){
		log.setShutdownData(p1, p2, p3);
	}

	public static Object logStream() {
		return log.getPrintStream();
	}
}
