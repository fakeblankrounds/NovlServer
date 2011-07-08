package com.fbrs.server.novl.staticweb;

import com.fbrs.server.Commands;
import com.fbrs.server.ServerEntry;
import com.fbrs.server.utils.ICommand;

public class Help implements ICommand{
	
	public static String HelpPage = "";
	
	@Override
	public String go(String... s) {

		return HelpPage;
	}
	
	public static void PackHelp()
	{
		System.out.println("Packageing help page");
		String head = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"> <html> <head> </head><body>";
		String tail = "</body></html>";
		String T1 = "<div style=\"border-style:solid; border-width:2px; background-color:pink;\"> Novl Server Version " + ServerEntry.version +	"</div>";
		String divs = "<div style=\"border-style:dotted; border-width:2px; background-color:lightblue; margin-left:0px;\">";
		String divs2 = "<div style=\"border-style:dotted; border-width:2px; background-color:lightblue; margin-left:25px;\">";
		String divs3 = "<div style=\"border-style:dotted; border-width:2px; background-color:lightblue; margin-left:50px;\">";
		String enddiv = "</div>";
		String T2 = "";
		
		String c = Commands.getRoot();
		String[] root = c.split(",");
		String[] second = new String[root.length];
		
		for(int i = 0; i < root.length; i++)
		{
			second[i] = Commands.Command.get(root[i]).getCommands("root");
			T2 += "<br>" + divs + root[i] + enddiv;
			for(String sec : second[i].split(","))
			{
				T2 += divs2 + sec + enddiv + divs3 + Commands.Command.get(root[i]).getCommands(sec) + enddiv;
			}
			
		}
		

		HelpPage = head + T1+ T2 + tail;
		
	}

	@Override
	public String getCommands(String s) {
		return "";
	}
}
