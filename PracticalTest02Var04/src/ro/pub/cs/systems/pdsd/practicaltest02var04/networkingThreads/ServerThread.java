package ro.pub.cs.systems.pdsd.practicaltest02var04.networkingThreads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.pdsd.practicaltest02var04.networkingThreads.CommunicationThread;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.systems.pdsd.practicaltest02var04.general.Constants;
import android.util.Log;

public class ServerThread extends Thread {
	private int port = 0;
	private ServerSocket serverSocket = null;

	private HashMap<String, String> data = null;

	public ServerThread(int port) {
		this.port = port;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			Log.e(Constants.TAG,
					"An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
		this.data = new HashMap<String, String>();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setServerSocker(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public synchronized void setData(String city, String webContent) {
		this.data.put(city, webContent);
	}

	public synchronized HashMap<String, String> getData() {
		return data;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				CommunicationThread communicationThread = new CommunicationThread(
						this, socket);
				communicationThread.start();
			} catch (ClientProtocolException clientProtocolException) {
				Log.e(Constants.TAG, "An exception has occurred: "
						+ clientProtocolException.getMessage());
				if (Constants.DEBUG) {
					clientProtocolException.printStackTrace();
				}
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "An exception has occurred: "
						+ ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			}

		}
	}

	public void stopThread() {
		if (serverSocket != null) {
			interrupt();
			try {
				serverSocket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "An exception has occurred: "
						+ ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			}
		}
	}

}
