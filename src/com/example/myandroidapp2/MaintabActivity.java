package com.example.myandroidapp2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import com.example.myandroidapp2.util.CommonUtils;
import com.example.myandroidapp2.util.MesssageTracer;

import dto.Person;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

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
				FileOutputStream outputStream=null;
				try {
					outputStream =openFileOutput("internal", Context.MODE_PRIVATE);//create file if not exist
					StringBuilder temp=new StringBuilder();
					LinkedList<String> messages=my.getMessages();
					for (String message:messages){temp.append(message+"\n");}
					outputStream.write(temp.toString().getBytes());
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
				if (buffer.length!=0) {
					while (input.read(buffer) >= 0) {}
					Intent intent = new Intent(this,DispalyStoredMessages.class);
					intent.putExtra(EXTRA_MESS, new String(buffer));
					startActivity(intent);
				}
			} catch (IOException e) {
				System.out.println("file not exist or reading error "+e.getMessage());
			}finally{
				try {
					input.close();
				} catch (IOException e) {
					System.out.println("file close error "+e.getMessage());
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
			System.out.println("21321213213 tabId-- "+tabId);
			if ("contacts".equals(tabId)){
				LinkedList<Person> contacts=CommonUtils.getPhoneContacts(mainContext);
				if (contacts!=null){
					String temp=contacts.toString();
					temp=temp.replaceAll(", ", "");
					temp=temp.replace("[", "");
					temp=temp.replace("]", "");
					contactsAdapter.add(temp);
				}
			}
		}
		
	}
}
