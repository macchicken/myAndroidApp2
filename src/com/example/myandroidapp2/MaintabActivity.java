package com.example.myandroidapp2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.myandroidapp2.backgroundSerive.ReadPhoneContacts;
import com.example.myandroidapp2.backgroundSerive.ReadStoredChat;
import com.example.myandroidapp2.backgroundSerive.StoreChatHistory;
import com.example.myandroidapp2.util.CommonUtils;
import com.example.myandroidapp2.util.MesssageTracer;

import dto.Person;

public class MaintabActivity extends Activity {

	public static final String EXTRA_MESS = "com.example.myandroidapp2.message";
	private String fileName="";
	private ListView chatView;
	private ListView contactsView;
	TabHost tabhost;
	Context mainContext;
	private myTabChangedListene l=new myTabChangedListene();
	private ArrayAdapter<String> itemsAdapter;
	private ArrayAdapter<String> contactsAdapter;
	
	private static MesssageTracer my = MesssageTracer.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintab);
		mainContext=getApplicationContext();
		fileName=mainContext.getFilesDir().getAbsolutePath()+"/internal";
		setupMultiTabs();
		chatView=(ListView) findViewById(R.id.chat_view);
		contactsView=(ListView) findViewById(R.id.contacts_view);
		itemsAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		contactsAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		chatView.setAdapter(itemsAdapter);
		contactsView.setAdapter(contactsAdapter);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setHomeButtonEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maintab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
			case R.id.action_settings:
				FileOutputStream outputStream;
				try {
					outputStream = openFileOutput("internal", Context.MODE_APPEND);
					if (outputStream!=null) {
						new StoreChatHistory() {
							@Override
							public void onPostExecute(Boolean result) {
								if (result.booleanValue()) {showAlert("save complete");}
							}
						}.execute(outputStream);
					}
				} catch (FileNotFoundException e) {
					showAlert("save error");
				}
				return true;
			case R.id.action_search:
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
	
	
	private void setupMultiTabs(){
		tabhost=(TabHost) findViewById(R.id.tabHost);
		tabhost.setOnTabChangedListener(l);
		tabhost.setup();
		
		TabSpec spec1=tabhost.newTabSpec("chat");
		spec1.setIndicator("chat");
		spec1.setContent(R.id.tab1);
		TabSpec spec2=tabhost.newTabSpec("chatview");
		spec2.setIndicator("chat view");
		spec2.setContent(R.id.tab2);
		TabSpec spec3=tabhost.newTabSpec("contacts");
		spec3.setIndicator("contacts");
		spec3.setContent(R.id.tab3);

		tabhost.addTab(spec1);
		tabhost.addTab(spec2);
		tabhost.addTab(spec3);
	}
	
	public void sendMessage(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		if (message!=null&&!"".equals(message.trim())) {
			CommonUtils.printMessagesFromList(itemsAdapter, message, "A");
		}
	}
	
	public void sendMessageB(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message_b);
		String message = editText.getText().toString();
		if (message!=null&&!"".equals(message.trim())) {
			CommonUtils.printMessagesFromList(itemsAdapter, message, "B");
		}
	}
	
	class myTabChangedListene implements TabHost.OnTabChangeListener{

		@Override	
		public void onTabChanged(String tabId) {
			if ("contacts".equals(tabId)){
				new ReadPhoneContacts(){
					@Override public void onPostExecute(String result){
						if (result!=null){contactsAdapter.add(result);}
					}
				}.execute(mainContext);
			}else if ("chatview".equals(tabId)){
				new ReadStoredChat(){
					@Override public void onPostExecute(String result){
						if (result!=null){
							TextView textView = (TextView) findViewById(R.id.stored_view);
							textView.setTextSize(20);
							textView.setText(result);
						}else{
							showAlert("no chat been stored,please use setting button to store some chats");
						}
					}
				}.execute(fileName);
			}
		}
		
	}
	
	private void showAlert(String alertMessage){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MaintabActivity.this);
		alertDialogBuilder.setMessage(alertMessage).setPositiveButton("ok", null);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	
}
