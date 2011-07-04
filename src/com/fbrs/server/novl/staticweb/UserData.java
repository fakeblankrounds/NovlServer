package com.fbrs.server.novl.staticweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.s3.NovlDataStore;
import com.fbrs.server.utils.ICommand;

public class UserData implements ICommand{

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
			if(request[2].equals("message"))
			{
				try{
					return Message(request[3], request[4], request[5], request[6], request[7]);
				}
				catch(Exception e)
				{
					return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
				}
			}
			if(request[2].equals("getmessages"))
			{
				try{
					return GetMessages(request[3], request[4]);
				}
				catch(Exception e)
				{
					return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
				}
			}
			if(request[2].equals("getsingle"))
			{
				try{
					return GetSingle(request[3], request[4], request[5]);
				}
				catch(Exception e)
				{
					return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
				}
			}
			if(request[2].equals("newUser"))
			{
				try{
					return Create(request[3], request[4]);

				}
				catch(Exception e)
				{
					return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
				}
			}
		}
		catch (Exception e)
		{
			return "Bad Request Contact Fake Blank Rounds Support at support@fakeblankrounds.com";
		}
		return "Default";
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

	//public String 


}
