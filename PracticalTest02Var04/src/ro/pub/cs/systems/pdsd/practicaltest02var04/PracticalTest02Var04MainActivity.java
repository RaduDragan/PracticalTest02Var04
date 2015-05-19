package ro.pub.cs.systems.pdsd.practicaltest02var04;

import ro.pub.cs.systems.pdsd.practicaltest02var04.general.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02var04.networkingThreads.ClientThread;
import ro.pub.cs.systems.pdsd.practicaltest02var04.networkingThreads.ServerThread;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var04MainActivity extends Activity {
	private EditText serverPortEditText = null;
	private Button connectButton = null;

	private EditText clientAddressEditText = null;
	private EditText clientPortEditText = null;
	private EditText urlEditText = null;

	private Button getWebContentButton = null;
	private WebView webView = null;

	private ClientThread clientThread = null;
	private ServerThread serverThread = null;

	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

	private class ConnectButtonClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View arg0) {
			String serverPort = serverPortEditText.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(getApplicationContext(),
						"Server port should be filled!", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			serverThread = new ServerThread(Integer.parseInt(serverPort));
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			} else {
				Log.e(Constants.TAG,
						"[MAIN ACTIVITY] Could not creat server thread!");
			}
		}
	}
	
private GetWebContentButtonClickListener getWebContentButtonClickListener = new GetWebContentButtonClickListener();
	
	private class GetWebContentButtonClickListener implements Button.OnClickListener{

		@Override
		public void onClick(View v) {
			String address = clientAddressEditText.getText().toString();
			String port = clientPortEditText.getText().toString();
			String url = urlEditText.getText().toString();
			if(address == null || address.isEmpty()
					|| port == null || port.isEmpty()){
				Toast.makeText(
						getApplicationContext(),
						"Client address and port should be filled!",
						Toast.LENGTH_SHORT
					).show();
					return;
			}
			
			if (serverThread == null || !serverThread.isAlive()) {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
			
			if(url == null || url.isEmpty()){
				Toast.makeText(
						getApplicationContext(),
						"Url should be filled!",
						Toast.LENGTH_SHORT
					).show();
					return;
			}
			webView.loadData(Constants.EMPTY_STRING, "text/html", "UTF-8");
			
			Log.e(Constants.TAG, "[MAIN ACTIVITY] url:" + url);
			clientThread = new ClientThread(
					address, 
					Integer.parseInt(port), 
					"http://" + url, 
					webView);
			
			clientThread.start();
		}
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var04_main);
		
		serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
		connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectButtonClickListener);
		
		clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
		clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
		urlEditText = (EditText)findViewById(R.id.url_edit_text);
		getWebContentButton = (Button)findViewById(R.id.get_web_content_button);
		getWebContentButton.setOnClickListener(getWebContentButtonClickListener);
		
		webView = (WebView)findViewById(R.id.web_view);
	}
	
	@Override
	protected void onDestroy() {
		if (serverThread != null) {
			serverThread.stopThread();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_var04_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
