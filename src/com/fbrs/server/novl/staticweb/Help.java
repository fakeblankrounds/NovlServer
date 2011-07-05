package com.fbrs.server.novl.staticweb;

import com.fbrs.server.ServerEntry;
import com.fbrs.server.utils.ICommand;

public class Help implements ICommand{
	@Override
	public String go(String s) {

		return "NovLServer _version_ " + ServerEntry.version;
	}
}
