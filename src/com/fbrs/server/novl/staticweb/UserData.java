package com.fbrs.server.novl.staticweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.s3.NovlDataStore;
import com.fbrs.server.utils.ICommand;

public class UserData implements ICommand{
	
	private static HashMap<String, ICommand> commands;

	public UserData()
	{
		commands = new HashMap<String, ICommand> ();
		commands.put("message", new ICommand(){

			@Override
			public String go(String... request) {
				return Message(request[0], request[1], request[2], request[3], request[4]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password, To, Subject, Message";
			}

		});
		commands.put("getmessages", new ICommand(){

			@Override
			public String go(String... request) {
				return GetMessages(request[0], request[1]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password";
			}

		});
		
		commands.put("getsingle", new ICommand(){

			@Override
			public String go(String... request) {
				return GetSingle(request[0], request[1], request[2]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password, MessageName";
			}

		});
		
		commands.put("newUser", new ICommand(){

			@Override
			public String go(String... request) {
				return  Create(request[0], request[1]);
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
					return commands.get(request[2]).go(request[3], request[4], request[5], request[6], request[7]);
				}
				catch(Exception e)
				{
					return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
				}
		}
		catch (Exception e)
		{
			return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
		}
	}

	public String Create(String username, String pass)
	{
		return NovlDataStore.CreateNewUser(username, pass);
	}

	public String Message(String username, String pass, String to, String head, String msg)
	{
		return NovlDataStore.AddUserMessages(username, pass, to,  head, msg);
	}

	public String GetMessages(String username, String pass)
	{
		return NovlDataStore.getUserMessages(username, pass);
	}
	
	public String GetSingle(String username, String pass, String msgname)
	{
		return NovlDataStore.getSingleMessage(username, pass, msgname);
	}

	@Override
	public String getCommands(String s) {

		if(s.equals("root"))
			return "message,getmessages,getsingle,newUser";
		else
			return commands.get(s).getCommands("");
	}

	//public String 


}
