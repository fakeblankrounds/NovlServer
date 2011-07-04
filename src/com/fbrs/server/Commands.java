package com.fbrs.server;

import java.util.HashMap;

import com.fbrs.server.novl.staticweb.Friends;
import com.fbrs.server.novl.staticweb.UserData;
import com.fbrs.server.utils.ICommand;
import com.fbrs.server.utils.UnitTests;

public class Commands {
	
	public static HashMap<String, ICommand> Command = new HashMap<String, ICommand>();
	public static void Populate()
	{
		Command.put("Friends", new Friends());
		Command.put("Users", new UserData());
		Command.put("runtest", new UnitTests());
	
	}

}
