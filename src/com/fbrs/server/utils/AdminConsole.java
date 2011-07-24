package com.fbrs.server.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;

public class AdminConsole {
	
public static final String AWSAccessKey = "AKIAI7IPRLC6XBKU6ATA";
		public  static final String AWSSecretKey = "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";
		public  static final AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKey, AWSSecretKey);
		
	
		private static final String Users = "Novl_UserStore";
		private static final String Friends = "Novl_FriendStore";
		private static final String Messages = "Novl_MsgStore";
		private static final String Groups = "Novl_GroupStore";
		private static final String Teams = "Novl_TeamStore";
		private static final String GroupList = "Novl_ListGroup";
		//private SecretKey novlkey = new SecretKey("");
		//private EncryptionMaterials encryption = new EncryptionMaterials(novlkey);
		

		public  static AmazonS3Client s3client = new AmazonS3Client(credentials); 
		public  static AmazonSimpleDBClient dbclient = new AmazonSimpleDBClient(credentials);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		reset();
	}
	
	public static void reset()
	{
		DeleteDomainRequest del = new DeleteDomainRequest(Groups);
		DeleteDomainRequest del1 = new DeleteDomainRequest(Teams);
		DeleteDomainRequest del2 = new DeleteDomainRequest(GroupList);
		CreateDomainRequest createDomainRequest = new CreateDomainRequest(Groups);
		CreateDomainRequest createDomainRequest1 = new CreateDomainRequest(Teams);
		CreateDomainRequest createDomainRequest2 = new CreateDomainRequest(GroupList);
		dbclient.deleteDomain(del);
		dbclient.deleteDomain(del1);
		dbclient.deleteDomain(del2);
		dbclient.createDomain(createDomainRequest);
		dbclient.createDomain(createDomainRequest1);
		dbclient.createDomain(createDomainRequest2);
		
	}

}
