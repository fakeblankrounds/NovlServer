package com.fbrs.server.novl.staticweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.s3.NovlDataStore;
import com.fbrs.server.utils.ICommand;

public class Friends implements ICommand{

	private static HashMap<String, ICommand> commands;

	public Friends()
	{
		commands = new HashMap<String, ICommand> ();
		commands.put("add", new ICommand(){

			@Override
			public String go(String... request) {
				return AddFriend(request[0], request[1], request[2]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password, Friend";
			}

		});
		commands.put("get", new ICommand(){

			@Override
			public String go(String... request) {
				return GetFriends(request[0], request[1]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password";
			}

		});
	}


	@Override
	public String go(String... s) {
		try {
			s[0] = URLDecoder.decode(s[0],"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		s[0] = StringEscapeUtils.escapeHtml(s[0]);
		try{
			String[] request = s[0].split("/");
			try{
				return commands.get(request[2]).go(request[3], request[4], request[5]);
			}
			catch (Exception e){
				return "Bad Query";
			}
		}
		catch(Exception e){
			return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
		}
	}

	public String AddFriend(String UserName, String password, String Friendname)
	{
		return NovlDataStore.AddUserFriend(UserName, password, Friendname);
	}

	public String GetFriends(String UserName, String password)
	{
		return NovlDataStore.getUserFriends(UserName, password);
	}


	@Override
	public String getCommands(String s) {
		if(s.equals("root"))
			return "add,get";
		else
			return commands.get(s).getCommands("");
	}
}
