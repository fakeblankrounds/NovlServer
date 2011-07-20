package com.fbrs.server.matchmaking;

public class InputProcessing {
	
	public static String Process(String s)
	{
		if(s.charAt(0) == '#')
			return s.substring(1);
		else
		{
			if(s.equals("300")){
				return null;
			}
		}
		return null;
	}

}
