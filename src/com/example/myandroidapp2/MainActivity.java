package com.example.myandroidapp2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myandroidapp2.util.CommonUtils;
import com.example.myandroidapp2.util.MesssageTracer;

public class MainActivity extends Activity {

	public static final String EXTRA_MESS = "com.example.myandroidapp2.message";
	public static final String EXTRA_MESS_B = "com.example.myandroidapp2.message_b";
	private static MesssageTracer my = MesssageTracer.getInstance();
	private TextView chatView;
	private String fileName="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		chatView=(TextView) findViewById(R.id.chat_view);
		fileName=getApplicationContext().getFilesDir().getAbsolutePath()+"/internal";
//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setHomeButtonEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
			case R.id.action_settings:
				FileOutputStream outputStream=null;
				try {
					outputStream =openFileOutput("internal", Context.MODE_PRIVATE);//create file if not exist
					String temp="";
					LinkedList<String> messages=my.getMessages();
					for (String message:messages){
						temp+=message;
					}
					outputStream.write(temp.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						outputStream.close();
					} catch (IOException e) {
					}
				}
				return true;
			case R.id.action_search:
			FileInputStream input = null;
			try {
				input= new FileInputStream(fileName);
				byte[] buffer = new byte[input.available()];
				while (input.read(buffer)>=0){
				}
				Intent intent = new Intent(this, DispalyStoredMessages.class);
				intent.putExtra(EXTRA_MESS, new String(buffer));
				startActivity(intent);
			} catch (IOException e) {
				System.out.println("file not exist or reading error");
			}finally{
				try {
					input.close();
				} catch (IOException e) {
				}
			}
				return true;
			case R.id.action_web:
				TextView webAddressView=(TextView) findViewById(R.id.web_address);
				String webAddress=webAddressView.getText().toString();
				Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webAddress));
				my.add("browse", webAddress);
				startActivity(webIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void sendMessage(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		if (message!=null&!"".equals(message.trim())) {
			Intent intent = new Intent(this, DisplayMessageActivity.class);
			intent.putExtra(EXTRA_MESS, message);
			startActivity(intent);
		}
	}
	
	public void sendMessageB(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message_b);
		String message = editText.getText().toString();
		if (message!=null&&!"".equals(message.trim())) {
			Intent intent = new Intent(this, DisplayMessageActivityB.class);
			intent.putExtra(EXTRA_MESS_B, message);
			startActivity(intent);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		CommonUtils.printMessages(chatView);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		CommonUtils.printMessages(chatView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		CommonUtils.printMessages(chatView);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}