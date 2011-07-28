package com.fbrs.server.novl.staticweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.ServerEntry;
import com.fbrs.server.db.NovlDataStore;
import com.fbrs.server.db.UserStatus;
import com.fbrs.server.utils.ICommand;

public class UserData implements ICommand{
	
	private static HashMap<String, ICommand> commands;

	public UserData()
	{
		commands = new HashMap<String, ICommand> ();
		commands.put("sendMessage", new ICommand(){

			@Override
			public String go(String... request) {
				return Message(request[0], request[1], request[2], request[3], request[4]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password, To, Subject, Message";
			}

		});
		commands.put("getMessages", new ICommand(){

			@Override
			public String go(String... request) {
				return GetMessages(request[0], request[1]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password";
			}

		});
		
		commands.put("getSingle", new ICommand(){

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
		
		commands.put("deleteUser", new ICommand(){

			@Override
			public String go(String... request) {
				return  Delete(request[0], request[1]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password";
			}

		});
		
		commands.put("setStatus", new ICommand(){

			@Override
			public String go(String... request) {
				return  setStatus(request[0], request[1], request[2]);
			}

			@Override
			public String getCommands(String s) {
				return "#, Username, Password, Status";
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
			String[] request = new String[8];
			String[] temp = s[0].split("/");
			int iter;
			if(request.length < temp.length)
				iter = request.length;
			else
				iter = temp.length;
			for(int i = 0; i < iter; i++)
				request[i] = temp[i];
			
				try{
					return commands.get(request[2]).go(request[3], request[4], request[5], request[6], request[7]);
				}
				catch(Exception e)
				{
					if(ServerEntry.verbose){
						e.printStackTrace();
						return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com \n";
					}
					else return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com \n";
				}
		}
		catch (Exception e)
		{
			if(ServerEntry.verbose){
				e.printStackTrace();
				return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com \n";
			}
			else return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com \n";
		}
	}

	public String Create(String username, String pass)
	{
		return NovlDataStore.CreateNewUser(username, pass);
	}
	
	public String Delete(String Username, String pass)
	{
		return NovlDataStore.DeleteUser(Username, pass);
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
	
	public String setStatus(String UserName, String password, String Status)
	{
		if(NovlDataStore.checkPassword(UserName, password)){
			UserStatus.setStatus(UserName, Status);
			return "300";
		}
		else
			return "Bad username or password";
			
	}
	

	@Override
	public String getCommands(String s) {

		if(s.equals("root"))
			return "setStatus,sendMessage,getMessages,getSingle,newUser,deleteUser";
		else
			return commands.get(s).getCommands("");
	}

	//public String 


}
