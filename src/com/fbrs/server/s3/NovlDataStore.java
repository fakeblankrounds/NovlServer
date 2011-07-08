package com.fbrs.server.s3;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.fbrs.server.utils.FBRSTimeStamp;

public class NovlDataStore {

	private static final String AWSAccessKey = "AKIAI7IPRLC6XBKU6ATA";
	private static final String AWSSecretKey = "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";
	private static final AWSCredentials credentials = new BasicAWSCredentials(
			AWSAccessKey, AWSSecretKey);

	private static final String Users = "Novl_UserStore";
	private static final String Friends = "Novl_FriendStore";
	private static final String Messages = "Novl_MsgStore";
	// private SecretKey novlkey = new SecretKey("");
	// private EncryptionMaterials encryption = new
	// EncryptionMaterials(novlkey);

	public static AmazonS3Client s3client = new AmazonS3Client(credentials);
	public static AmazonSimpleDBClient dbclient = new AmazonSimpleDBClient(
			credentials);

	//private static List<ReplaceableAttribute> AttrList;

	public static List<ReplaceableAttribute> getUserAttr(String password) {
		List<ReplaceableAttribute> AttrList;
		AttrList = new ArrayList<ReplaceableAttribute>();
		AttrList.add(new ReplaceableAttribute("password", password, false));
		AttrList.add(new ReplaceableAttribute("status", "busy", true));
		AttrList.add(new ReplaceableAttribute("online", "offline", true));

		return AttrList;
	}

	private static List<ReplaceableAttribute> addItem(String name, String value) {
		List<ReplaceableAttribute> AttrList = new ArrayList<ReplaceableAttribute>();
		AttrList.add(new ReplaceableAttribute(name, value, false));
		return AttrList;
	}

	private static boolean checkPassword(String UserName, String password) {
		GetAttributesRequest request = new GetAttributesRequest(Users, UserName)
				.withAttributeNames("password");
		GetAttributesResult r = dbclient.getAttributes(request);
		Attribute a = r.getAttributes().get(0);
		if (a.getName().equals("password") && a.getValue().equals(password))
			return true;
		return false;
	}
	
	private static boolean doesUserExist(String UserName)
	{
		GetAttributesRequest request = new GetAttributesRequest(Users, UserName);
		GetAttributesResult r = dbclient.getAttributes(request);
		if(r.getAttributes().size() == 0)
			return false;
		return true;
	}

	private static String getStatus(String UserName) {
		try {
			GetAttributesRequest request = new GetAttributesRequest(Users,
					UserName).withAttributeNames("status", "online");
			GetAttributesResult r = dbclient.getAttributes(request);
			String r_string = UserName;
			Attribute a = r.getAttributes().get(0);
			Attribute b = r.getAttributes().get(1);
			r_string += "-" + a.getValue() + "-" + b.getValue();
			return r_string;
		} catch (Exception e) {
			return "";
		}
	}
	
	@SuppressWarnings("unused")
	private static String setStatus(String UserName, String password)
	{
		return "";
	}

	public static String CreateNewUser(String UserName, String password) {
		try {
			if(UserName.contains("/"))
				return "Username cannot contain '/'";
			if (!doesUserExist(UserName)) {
				PutAttributesRequest p = new PutAttributesRequest(Users,
						UserName, getUserAttr(password));
				PutAttributesRequest a = new PutAttributesRequest(Messages,
						UserName, addItem("Messages", "welcome"));
				PutAttributesRequest b = new PutAttributesRequest(Friends,
						UserName, addItem("Friend", "Support"));
				dbclient.putAttributes(p);
				dbclient.putAttributes(a);
				dbclient.putAttributes(b);
				return "300"; // ok code

			}
			return "Username Already exists";
		} catch (Exception e) {
			return "Error Occured. We are working on it";
		}
	}
	
	public static String DeleteUser(String UserName, String password)
	{
		try {

			if (!checkPassword(UserName, password))
				return "Bad Username or password";

			DeleteAttributesRequest del = new DeleteAttributesRequest(Users, UserName);
			DeleteAttributesRequest del2 = new DeleteAttributesRequest(Friends, UserName);
			DeleteAttributesRequest del3 = new DeleteAttributesRequest(Messages, UserName);
			dbclient.deleteAttributes(del);
			dbclient.deleteAttributes(del2);
			dbclient.deleteAttributes(del3);
			//s3client.deleteObject("NovlDataStore_msg", UserName);
			
			return "300";

		} catch (Exception e) {
			return "Something went wrong we are working on it"; // ok code
		}
	}

	public static String AddUserFriend(String UserName, String password,
			String Friendname) {
		try {

			if (!checkPassword(UserName, password))
				return "Bad Username or password";

			PutAttributesRequest p = new PutAttributesRequest(Friends,
					UserName, addItem("Friend", Friendname));
			dbclient.putAttributes(p);
			return "300";

		} catch (Exception e) {
			return "Something went wrong we are working on it"; // ok code
		}
	}

	public static String AddUserMessages(String UserName, String password,
			String To, String messageheader, String message) {
		try {
			if (!checkPassword(UserName, password))
				return "Bad Username or password";
			if(!doesUserExist(To))
				return "Recipient Doesn't Exist";
			String key = To+ ";" + messageheader + ";" + FBRSTimeStamp.getDateTime();

			PutAttributesRequest p = new PutAttributesRequest(Messages,
					To, addItem("Message", key));
			dbclient.putAttributes(p);
			
			ByteArrayInputStream bs = new ByteArrayInputStream(message.getBytes());
			s3client.putObject("NovlDataStore_msg", key, bs, null);
			
			
			return "300";
			// String m = "From: " + UserName + "/" + messageheader + "/" +
			// message + "/Sent: " + new Date().toString() + '\n';

		} catch (Exception e) {
			return "Either Bad User or Something went wrong"; // ok code
		}
	}

	public static void AddUserGameStats(String UserName, String stats) {

	}

	public static String getUserFriends(String UserName, String password) {
		try {
			if (!checkPassword(UserName, password))
				return "Bad Username or password";

			GetAttributesRequest request = new GetAttributesRequest(Friends,
					UserName);
			GetAttributesResult r = dbclient.getAttributes(request);
			String r_string = "";
			for (Attribute a : r.getAttributes()) {
				r_string += getStatus(a.getValue()) + "/";

			}
			return r_string;
		} catch (Exception e) {
			return "Something went wrong, we are working on it";
		}

	}

	public static String getUserMessages(String UserName, String password) {
		try {
			if (!checkPassword(UserName, password))
				return "Bad Username or password";

			GetAttributesRequest request = new GetAttributesRequest(Messages,
					UserName);
			GetAttributesResult r = dbclient.getAttributes(request);
			String r_string = "";
			for (Attribute a : r.getAttributes()) {
				r_string += StringEscapeUtils.escapeHtml(a.getValue()) + "#";
			}
			return r_string;
		} catch (Exception e) {
			return "Something went wrong, we are working on it";
		}
	}
	
	public static String getSingleMessage(String UserName, String password, String messageName)
	{
		try {
			if (!checkPassword(UserName, password))
				return "Bad Username or password";

			
			S3Object msg = s3client.getObject("NovlDataStore_msg", messageName);
			BufferedReader read = new BufferedReader(new InputStreamReader(msg.getObjectContent()));
			String s = "";
			String return_ = "";
			while((s = read.readLine() )!= null)
			{
				return_ += s;
			}

			return return_;
			// String m = "From: " + UserName + "/" + messageheader + "/" +
			// message + "/Sent: " + new Date().toString() + '\n';

		} catch (Exception e) {
			return "Something went wrong we are working on it"; // ok code
		}
		
	}

	public static String getUserGameStats(String UserName) {
		return "";
	}
}