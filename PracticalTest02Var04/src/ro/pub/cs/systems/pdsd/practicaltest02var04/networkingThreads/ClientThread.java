package ro.pub.cs.systems.pdsd.practicaltest02var04.networkingThreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.pdsd.practicaltest02var04.general.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02var04.general.Utilities;
import android.util.Log;
import android.webkit.WebView;

public class ClientThread extends Thread {
	private String url;
	private String address;
	private int port;
	private WebView webView;

	private Socket socket;

	public ClientThread(String address, int port, String url, WebView webView) {
		this.address = address;
		this.port = port;
		this.url = url;
		this.webView = webView;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter printWriter = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(url);
				printWriter.flush();

				String webContent;
				StringBuilder buildWebContent = new StringBuilder();
				while ((webContent = bufferedReader.readLine()) != null) {
					buildWebContent.append(webContent);
				}
				buildWebContent.append("\n");
				final String finalizedWebContent = buildWebContent.toString();
				webView.post(new Runnable() {

					@Override
					public void run() {
						webView.loadDataWithBaseURL(url, finalizedWebContent,
								"text/html", "UTF-8", null);

					}
				});
			} else {
				Log.e(Constants.TAG,
						"[CLIENT THREAD] bufferedReader/printWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: "
					+ ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}
}
