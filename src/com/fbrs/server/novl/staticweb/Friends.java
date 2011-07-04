package com.fbrs.server.novl.staticweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.s3.NovlDataStore;
import com.fbrs.server.utils.ICommand;

public class Friends implements ICommand{
	
	public  String getFriends(String user, String pass)
	{
		return "John-ingame-online,Bill-online";
	}

	
	public  String Encode(String key, String s)
	{
	 return null;
	}


	@Override
	public String go(String s) {
		try {
			s = URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		s = StringEscapeUtils.escapeHtml(s);
		try{
			String[] request = s.split("/");
			if(request[2].equals("add"))
			{
				//User/pass/friend
				return AddFriend(request[3], request[4], request[5]);
			}
			if(request[2].equals("get"))
			{
				return GetFriends(request[3], request[4]);
			}
			return "Bad Query";
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
}
