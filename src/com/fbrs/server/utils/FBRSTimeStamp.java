package com.fbrs.server.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FBRSTimeStamp {
	
	public static String getDateTime() {
		
        DateFormat dateFormat = new SimpleDateFormat("MM;dd;yy;HH;mm;ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
