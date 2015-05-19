package ro.pub.cs.systems.pdsd.practicaltest02var04.networkingThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import ro.pub.cs.systems.pdsd.practicaltest02var04.general.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02var04.general.Utilities;
import android.util.Log;

public class CommunicationThread extends Thread {
	private ServerThread serverThread;
	private Socket socket;

	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			BufferedReader bufferedReader;
			try {
				bufferedReader = Utilities.getReader(socket);
				PrintWriter printWriter = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					String url = bufferedReader.readLine();
					HashMap<String, String> data = serverThread.getData();
					String webContent= null;
					
					if (url != null && !url.isEmpty()){
						if (data != null && data.containsKey(url)) {
							Log.i(Constants.TAG,
									"[COMMUNICATION THREAD] Getting the information from the cache...");
							webContent = data.get(url);
						} else {
							Log.i(Constants.TAG,
									"[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet(url);
							
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String webResponse = httpClient.execute(
									httpGet, responseHandler);
							if (webResponse != null) {
								webContent = webResponse;
								serverThread.setData(url, webResponse);
							}
						}
						
						if (webContent != null) {
							Log.e(Constants.TAG,
									"[COMMUNICATION THREAD] result = " + webContent);
							if (webContent != null) {
								printWriter.println(webContent);
								printWriter.flush();
							}
						}else {
							Log.e(Constants.TAG,
									"[COMMUNICATION THREAD] Web Response is null!");
						}
					}else {
						Log.e(Constants.TAG,
								"[COMMUNICATION THREAD] Error receiving URL from client!");
					}
				}
				else {
					Log.e(Constants.TAG,
							"[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			}catch (IOException ioException) {
				Log.e(Constants.TAG,
						"[COMMUNICATION THREAD] An exception has occurred: "
								+ ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			}
		}
		else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] socket is null!");
		}
	}
}
