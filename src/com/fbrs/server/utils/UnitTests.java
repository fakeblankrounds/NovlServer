package com.fbrs.server.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.fbrs.server.Commands;
import com.fbrs.server.ServerEntry;

public class UnitTests implements ICommand{

	private static HashMap<String, ICommand> commands;

	private static final String user1 = "FBRS_SYSTEM_RUNTIME_TEST_USER1";
	private static final String user2 = "FBRS_SYSTEM_RUNTIME_TEST_USER2";
	private static final String u1pass= "2j2333094*h3j428320)#!@*$@#LKJH#U$)(#@$JLK#@$812j31hjkn,asdf";
	private static final String u2pass= "#(@#$8kjl13409u(*JH@#$32j4hHJ#()@KL432hj*#@$KJH@#$0#$JH@#$89";
	private static final String msgHeader1 = "USERUNITTESTMESSAGE";
	private static final String msgSubject1 = "MSGSUBJECT1USERMESSAGESUBJECT";
	private static final String group = "FBRS_SYSTEM_TEST_GROUP";
	private static final String team = "FBRS_SYSTEM_TEST_TEAM";
	
	//HTML
	private static String passed = "<div style=\"border-style:dotted; border-width:2px;  background-color:lightgreen;\">Passed</div> \n";
	private static String test_stat = "<div style=\"border-style:dotted; border-width:2px; background-color:lightblue;\">";
	private static String _test_stat = "</div> \n";
	private static String failed = "<div style=\"border-style:dotted; border-width:2px; background-color:pink;\">Failed</div> \n";
	private static String info = "<div style=\"border-style:solid; border-width:2px; background-color:lightyellow;\">";
	private static String _info = "</div> \n";
	public UnitTests()
	{
		if(ServerEntry.unittest){
			commands = new HashMap<String, ICommand> ();
			commands.put("All", new ICommand(){

				@Override
				public String go(String... request) {
					return TestAll();
				}

				@Override
				public String getCommands(String s) {
					return "#";
				}

			});
			commands.put("Friends", new ICommand(){

				@Override
				public String go(String... request) {
					return TestFriends();
				}

				@Override
				public String getCommands(String s) {
					return "#";
				}

			});
			commands.put("Users", new ICommand(){

				@Override
				public String go(String... request) {
					return TestUsers();
				}

				@Override
				public String getCommands(String s) {
					return "#";
				}

			});

			commands.put("Groups", new ICommand(){

				@Override
				public String go(String... request) {
					return TestGroups();
				}

				@Override
				public String getCommands(String s) {
					return "#";
				}

			});

			commands.put("End", new ICommand(){

				@Override
				public String go(String... request) {
					return FinishTestUsers();
				}

				@Override
				public String getCommands(String s) {
					return "#";
				}

			});

		}
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
			for(int i = 0; i < temp.length; i++)
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

	@Override
	public String getCommands(String s) {
		if(ServerEntry.unittest){
			if(s.equals("root"))
				return "All,Friends,Users,Groups,End";
			else
				return commands.get(s).getCommands("");
		}
		else
			return "";
	}

	public String TestAll()
	{
		String r_value = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"> <html>  <head>  </head><body>";
		r_value +=test_stat +  "Starting Users Test:\n" + _test_stat;
		r_value += TestUsers(); 
		r_value += test_stat + "Starting Friend Test:\n" + _test_stat;
		r_value += TestFriends();
		r_value +=test_stat + "Testing Groups" + _test_stat;
		r_value += TestGroups();
		r_value +=test_stat +  "Finishing Users Test:\n" +_test_stat;
		r_value += FinishTestUsers();
		
		r_value += "Testing Done! </body> </html>";
		return r_value;
	}

	
	public String TestFriends()
	{
		ICommand c = Commands.Command.get("Friends");
		String r_value = test_stat + "FriendTest 1: Adding "+ user2 +" to "+ user1 + "\'s friendList \n" + _test_stat;
		if(c.go("//add/" + user1 + "/" + u1pass + "/" + user2 + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;

		r_value += test_stat + "FriendTest 2: Adding "+ user1 +" to "+ user2 + "\'s friendList \n" + _test_stat;
		if(c.go("//add/" + user2 + "/" + u2pass + "/" + user1 + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;

		r_value += test_stat + "FriendTest 3: Checking Double "+ user1 +" to "+ user2 + "\'s friendList \n" + _test_stat;
		if(c.go("//add/" + user2 + "/" + u2pass + "/" + user1 + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;

		r_value += c.go("//get/" + user1 + "/" + u1pass + "/") + "<br>";
		r_value += c.go("//get/" + user2 + "/" + u2pass + "/") + "<br>";

		return r_value;
	}
	public String TestUsers()
	{
		String r_value = "";
		String e;
		ICommand c = Commands.Command.get("Users");
		r_value +=test_stat +  "UserTest 1: Adding "+ user1 +"\n" + _test_stat;
		if(c.go("//newUser/" + user1 + "/" + u1pass + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;

		r_value += test_stat +  "UserTest 2: Adding "+ user2 +"\n" + _test_stat;
		if(c.go("//newUser/" + user2 + "/" + u2pass + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;

		r_value += test_stat +  "UserTest 3: Messageing "+ user1 + " from " +user2 +"\n" + _test_stat;
		if((e = c.go("//message/" + user2 + "/" + u2pass + "/" + user1 +"/" + msgHeader1 + "/" + msgSubject1 + "/")).equals("300"))
			r_value+=passed;
		else
			r_value+=failed + e;

		r_value += test_stat +  "UserTest 4: Messageing "+ user2 + " from " +user1 +"\n" + _test_stat;
		if((e = c.go("//message/" + user1 + "/" + u1pass + "/" + user2 +"/" + msgHeader1 + "/" + msgSubject1 + "/")).equals("300"))
			r_value+=passed;
		else
			r_value+=failed + e;

		r_value += test_stat +  "UserTest 5: Getting messages for "+ user1 +"\n" + _test_stat;
		r_value += c.go("//getmessages/" + user1 + "/" + u1pass + "/" );
		r_value += test_stat +  "UserTest 6: Getting messages for "+ user2 +"\n" + _test_stat;
		String t = c.go("//getmessages/" + user2 + "/" + u2pass + "/" );
		r_value += t;
		r_value += test_stat +  "UserTest 7: Getting messages for "+ user1 +"\n" + _test_stat;
		if(t.split("#").length > 1)	
			r_value += c.go("//getsingle/" + user1 + "/" + u1pass + "/" + t.split("#")[1] + "/" );
		else
			r_value += failed ;
		//r_value += t.split("#").length;
		//if(c.go("//getmessages/" + user1 + "/" + u1pass + "/" ).equals("300"))
		//	r_value+=passed;
		//else
		//	r_value+=failed;


		return r_value;
	}

	public String FinishTestUsers()
	{

		String r_value = "";
		ICommand c = Commands.Command.get("Users");
		r_value += test_stat +  "UserTest 1: Deleteing "+ user1 +"\n" + _test_stat;
		if(c.go("//deleteUser/" + user1 + "/" + u1pass + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;

		r_value += test_stat + "UserTest 2: Deleteing "+ user2 +"\n" + _test_stat;
		if(c.go("//deleteUser/" + user2 + "/" + u2pass + "/").equals("300"))
			r_value+=passed;
		else
			r_value+=failed;
		return r_value;
	}

	public String TestGroups()
	{
		String r_value = "";
		//ICommand c = Commands.Command.get("Groups");
		r_value += RunTest("Groups", "//addGroup/" + user1 + "/" + u1pass + "/" + group + "/f/", "GroupTest1: Createing " + group + " for " + user1);
		r_value += RunTest("Groups", "//addGroup/" + user1 + "/" + u1pass + "/" + group + "/f/", "GroupTest2: Createing Duplicate" + group + " for " + user1);
		r_value += RunTest("Groups", "//addGroup/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest3: Createing " + team + " for " + user1);
		r_value += RunTest("Groups", "//addGroup/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest4: Createing Duplicate" + team + " for " + user1);
		r_value += RunVisual("Groups", "//getGroups/" + user1 + "/" + u1pass + "/f/", "GroupTest5: Getting Groups" );
		r_value += RunVisual("Groups", "//getGroups/" + user1 + "/" + u1pass + "/t/", "GroupTest6: Getting Teams" );
		r_value += RunVisual("Groups", "//getMembers/" + user1 + "/" + u1pass + "/" + group + "/f/", "GroupTest7: Getting group members" );
		r_value += RunVisual("Groups", "//getMembers/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest8: Getting Team members" );
		r_value += RunTest("Groups", "//addUsertoTeam/" + user1 + "/" + u1pass + "/" + team + "/" + user2 + "/", "GroupTest9: adding user to :" + team + " for " + user2);
		r_value += RunVisual("Groups", "//getMembers/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest8: Getting group members" );
		r_value += RunTest("Groups", "//addAdmin/" + user1 + "/" + u1pass + "/" + team + "/" + user2 + "/t/", "GroupTest10: adding Admin to :" + team + " for " + user2);
		r_value += RunVisual("Groups", "//getMembers/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest8: Getting group members" );
		r_value += RunTest("Groups", "//removeUserFromGroup/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest11: removeing :" + user1 + " from " + team);
		r_value += RunTest("Groups", "//removeUserFromGroup/" + user2 + "/" + u2pass + "/" + team + "/t/", "GroupTest12: removeing :" + user2 + " from " + team);
		r_value += RunTest("Groups", "//removeGroup/" + user1 + "/" + u1pass + "/" + team + "/t/", "GroupTest13: removing  " + team + " for " + user2);
		r_value += RunTest("Groups", "//removeGroup/" + user1 + "/" + u1pass + "/" + group + "/f/", "GroupTest13: removing  " + group + " for " + user1);
		return r_value;
	}
	
	public String RunTest(String command, String url, String description)
	{
		String r_value = "";
		String testresult;
		ICommand c = Commands.Command.get(command);
		r_value += test_stat + description + _test_stat;
		testresult = c.go(url);
		if(testresult.equals("300"))
			r_value+=passed;
		else
			r_value+=failed + testresult + "Call: " + url;
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r_value;
	}
	
	public String RunVisual(String command, String url, String description)
	{
		String r_value = "";
		String testresult;
		ICommand c = Commands.Command.get(command);
		r_value += test_stat + description + _test_stat;
		testresult = c.go(url);
		r_value+= info + testresult + _info;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r_value;
	}
}
