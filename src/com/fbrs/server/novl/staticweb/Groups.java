package com.fbrs.server.novl.staticweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.db.NovlDataStore;
import com.fbrs.server.utils.ICommand;

public class Groups  implements ICommand{

	private static HashMap<String, ICommand> commands;

	public Groups()
	{
		commands = new HashMap<String, ICommand> ();
		commands.put("addGroup", new ICommand(){

			@Override
			public String go(String... request) {
				return AddGroup(request[0], request[1], request[2], request[3]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, GroupName, isPrivate(t/f)";
			}

		});
		
		commands.put("removeGroup", new ICommand(){

			@Override
			public String go(String... request) {
				return RemoveGroup(request[0], request[1], request[2], request[3]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, GroupName, isPrivate(t/f)";
			}

		});
		
		commands.put("addUsertoTeam", new ICommand(){

			@Override
			public String go(String... request) {
				return AddUsertoTeam(request[0], request[1], request[2], request[3]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, GroupName, user";
			}

		});
		
		commands.put("addAdmin", new ICommand(){

			@Override
			public String go(String... request) {
				return AddAdmin(request[0], request[1], request[2], request[3], request[3]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, GroupName, user";
			}

		});
		
		commands.put("removeUserFromGroup", new ICommand(){

			@Override
			public String go(String... request) {
				return RemoveUser(request[0], request[1], request[2], request[3]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, GroupName, isTeam (t/f)";
			}

		});
		
		commands.put("getGroups", new ICommand(){

			@Override
			public String go(String... request) {
				return GetGroups(request[0], request[1], request[2]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, isTeam (t/f)";
			}

		});
		
		commands.put("getMembers", new ICommand(){

			@Override
			public String go(String... request) {
				return GetGroupMemebers(request[0], request[1], request[2], request[3]);
			}

			@Override
			public String getCommands(String s) {
				return "#, UserName, password, GroupName, isTeam (t/f)";
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
			String[] request = new String[7];
			String[] temp = s[0].split("/");
			int iter;
			if(request.length < temp.length)
				iter = request.length;
			else
				iter = temp.length;
			for(int i = 0; i < iter; i++)
				request[i] = temp[i];
			try{
				return commands.get(request[2]).go(request[3], request[4], request[5], request[6]);
			}
			catch (Exception e){
				e.printStackTrace();
				return "Bad Query";
			}
		}
		catch(Exception e){
			return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
		}
	}

	//CreateNewGroup(String UserName, String password, String GroupName, boolean isPrivate)
	public String AddGroup(String UserName, String password, String GroupName, String p)
	{
		boolean isPrivate;
		if(p.equals("t"))
			isPrivate = true;
		else
			isPrivate = false;
		
		return NovlDataStore.CreateNewGroup(UserName, password, GroupName, isPrivate);
	}
	
	//RemoveGroup(String UserName, String password, String GroupName, boolean isPrivate)
	public String RemoveGroup(String UserName, String password, String GroupName, String p)
	{
		boolean isPrivate;
		if(p.equals("t"))
			isPrivate = true;
		else
			isPrivate = false;
		
		return NovlDataStore.RemoveGroup(UserName, password , GroupName, isPrivate);
	}
	
	//AddUserToGroup(String UserName, String password, String GroupName, String user)
	public String AddUsertoTeam(String UserName, String password, String GroupName, String user)
	{
		return NovlDataStore.AddUserToGroup(UserName, password, GroupName, user);
	}
	
	//AddAdmintoGroup(String UserName, String password, String GroupName, String user)
	public String AddAdmin(String UserName, String password, String GroupName, String user, String p)
	{
		boolean isPrivate;
		if(p.equals("t"))
			isPrivate = true;
		else
			isPrivate = false;
		return NovlDataStore.AddAdmintoGroup(UserName, password, GroupName, user, isPrivate);
	}
	
	//RemoveUserFromGroup(String UserName, String password, String GroupName)
	public String RemoveUser(String UserName, String password, String GroupName, String p)
	{
		boolean isPrivate;
		if(p.equals("t"))
			isPrivate = true;
		else
			isPrivate = false;
		
		return NovlDataStore.RemoveUserFromGroup(UserName, password, GroupName, isPrivate);
	}
	//getListOfGroups(String UserName, String password, boolean isTeam)
	public String GetGroups(String UserName, String password, String Team)
	{
		boolean isPrivate;
		if(Team.equals("t"))
			isPrivate = true;
		else
			isPrivate = false;
		return NovlDataStore.GetListOfGroups(UserName, password, isPrivate);
	}
	
	//GetListOfGroupMembers(String UserName, String password, String GroupName, boolean isTeam)
	public String GetGroupMemebers(String UserName, String password, String GroupName, String p)
	{
		boolean isPrivate;
		if(p.equals("t"))
			isPrivate = true;
		else
			isPrivate = false;
		
		return NovlDataStore.GetListOfGroupMembers(UserName, password, GroupName, isPrivate);
	}

	@Override
	public String getCommands(String s) {
		if(s.equals("root"))
			return "addGroup,removeGroup,addUsertoTeam,addAdmin,removeUserFromGroup,getGroups,getMembers";
		else
			return commands.get(s).getCommands("");
	}
}
