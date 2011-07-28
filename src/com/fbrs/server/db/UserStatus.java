package com.fbrs.server.db;

import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class UserStatus extends TimerTask{
	
	private static ConcurrentHashMap<String, String> usermap = new ConcurrentHashMap<String, String>(100);
	
	public static String getStatus(String UserName)
	{
		String s = usermap.get(UserName);
		if(s == null)
			s = "null-offline";
		else
			return usermap.get(UserName);
		return s;
	}
	
	public static void setStatus(String UserName, String Status)
	{
		usermap.put(UserName, Status);
	}
	
	public static void ClearStatus()
	{
		usermap.clear();
	}

	@Override
	public void run() {
		UserStatus.ClearStatus();
	}
}
