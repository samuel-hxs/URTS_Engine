package term;

public class Terminal implements CommandAble{

	public String[] commands;
	public CommandAble[] terminals;
	
	public Terminal(String[] c, CommandAble[] t){
		setChoosable(c, t);
	}
	
	public void setChoosable(String[] c, CommandAble[] t){
		commands = c;
		terminals = t;
		if(c != null)
		for (int i = 0; i < c.length; i++) {
			if(c[i].length()>0 && t[i] instanceof Terminal)
				if(c[i].charAt(c[i].length()-1) != '.')
					c[i] += ".";
			if(c[i].length()>0 && t[i] instanceof BasicCommand)
				if(c[i].charAt(c[i].length()-1) != ' ')
					c[i] += " ";
		}
	}

	@Override
	public void exec(String s, TermPrint answer) throws TerminalExeption {
		for (int i = 0; i < commands.length; i++) {
			if(s.startsWith(commands[i])){
				terminals[i].exec(s.substring(commands[i].length()), answer);
				return;
			}
		}
		answer.println("Can't resolve: "+s, TermColors.COMERR);
	}

	@Override
	public String getToolTip(String s) {
		for (int i = 0; i < commands.length; i++) {
			if(s.startsWith(commands[i])){
				return terminals[i].getToolTip(s.substring(commands[i].length()));
			}
		}
		return null;
	}

	@Override
	public String[] getChoosing(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < commands.length; i++) {
			if(s.startsWith(commands[i].toLowerCase())){
				return terminals[i].getChoosing(s.substring(commands[i].length()));
			}
		}
		int u = 0;
		for (int i = 0; i < commands.length; i++) {
			if(commands[i].toLowerCase().startsWith(s))
				u++;
		}
		String[] k = new String[u];
		u = 0;
		for (int i = 0; i < commands.length; i++) {
			if(commands[i].toLowerCase().startsWith(s)){
				k[u] = commands[i];
				u++;
			}
		}
		return k;
	}
}
