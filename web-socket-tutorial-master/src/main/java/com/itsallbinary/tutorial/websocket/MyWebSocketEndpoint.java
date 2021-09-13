package com.itsallbinary.tutorial.websocket;

import java.io.File;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Indicates that this class is a websocket endpoint with URL "/server-endpoint"
 */
@ServerEndpoint("/server-endpoint")
public class MyWebSocketEndpoint {

	/**
	 * Container calls this method when browser connects to this endpoint.
	 */
	//private static Set<Session> allSessions;
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Session Open [" + session.getId() + "]");

		// Start a separate thread which will send messages back to browser.
		//allSessions = session.getOpenSessions();
		new Thread(new ServerToBrowserMessageSender(session)).start();
	}

	/**
	 * Container calls this method when websocket connection is closed by browser or
	 * server.
	 */
	@OnClose
	public void onClose(Session session) {
		System.out.println("Session Close [" + session.getId() + "]");
	}

	/**
	 * Process message received from browser.
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Message received [" + session.getId() + "] Message=" + message);

	}

	/**
	 * Container calls this method when there is an error in websocket
	 * communication.
	 */
	@OnError
	public void onError(Throwable t) {
		System.out.println("Error - " + t.getMessage());
	}
}

/**
 * This is a custom thread which sends message to browser every 2000
 * milliseconds.
 */
class ServerToBrowserMessageSender extends Thread {

	//private int count = 0;
	private Session session;
	static long sleepTime = 1000;
	private static long timeStamp=0;
	public ServerToBrowserMessageSender(Session session) {
		this.session = session;
	}

	@Override
	public void run() {
		//static long sleepTime = 1000;
		//private static long timeStamp=0;
		File file = new File("C:\\Users\\namandubey\\Desktop\\p.txt");
		int lines = 10;
		//timeStamp = file.lastModified();
		while(true) {
		if(timeStamp!=file.lastModified()) {	
		int readLines = 0;
	
	  StringBuilder builder = new StringBuilder();
	  RandomAccessFile randomAccessFile = null;
	  try {
	    randomAccessFile = new RandomAccessFile(file, "r");
	    long fileLength = file.length() - 1;
	    // Set the pointer at the last of the file
	    randomAccessFile.seek(fileLength);
	    for(long pointer = fileLength; pointer >= 0; pointer--){
	      randomAccessFile.seek(pointer);
	      char c;
	      // read from the last one char at the time
	      c = (char)randomAccessFile.read(); 
	      // break when end of the line
	      if(c == '\n'){
	        readLines++;
	        if(readLines == lines)
	         break;
	      }
	      builder.append(c);
	    }
	    // Since line is read from the last so it 
	    // is in reverse so use reverse method to make it right
	    builder.reverse();
	    System.out.println("Line - " + builder.toString());
	    String str = builder.toString();
	    str = str.replaceAll("(\r\n|\n)", "<br />");
	    session.getBasicRemote()
		.sendText("Line - " + str);
	    timeStamp = file.lastModified();
	  } 
	  
	  catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  }
	  catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  }finally{
	    if(randomAccessFile != null){
	      try {
	        randomAccessFile.close();
	      } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	    }
	  }
	  continue;
		}
		try {
	          Thread.sleep(sleepTime);
	        } catch (InterruptedException e) {
	          Thread.currentThread().interrupt();
	          break;
	        }
		//long timeStamp = file.lastModified();
		}
	}
	  
	  
	  
	  
		/*while (count < 5) {
			count++;
			try {
				// Send message to browser ever 2000 milliseconds.
				Thread.sleep(2000);
				session.getBasicRemote()
						.sendText("[Server -> Browser] A Message from server to browser. Count = " + count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}
