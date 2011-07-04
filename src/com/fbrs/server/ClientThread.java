package com.fbrs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.fbrs.server.utils.ICommand;

public class ClientThread implements Runnable{
	private Socket clientSocket = null;

	String prnt;
	
	public ClientThread(Socket socket)
	{
		this.clientSocket = socket;
	}

	public void run()
	{

		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(
					clientSocket.getOutputStream(), true);

			String request = in.readLine();
			String[] tokens = request.split("/");
			ICommand command = Commands.Command.get(tokens[1]);
			if(command != null)
				prnt = command.go(request);
				out.println(prnt);
				if(ServerEntry.verbose) System.out.println(prnt);



			System.out.println(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
