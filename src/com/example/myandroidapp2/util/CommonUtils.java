package com.example.myandroidapp2.util;

import java.util.LinkedList;

import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CommonUtils {
	
	private static MesssageTracer my = MesssageTracer.getInstance();
	
	//print the send messages from tracer instance
	public static void printMessages(final TextView chatView){
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					String temp = "";
					LinkedList<String> messages=my.getMessages();
						for (String message : messages) {
							temp += message + "\r\n";
						}
						chatView.setText(temp);
				}
			}, 750);
	}
	
	//save message to instance
	public static void printMessagesFromList(ArrayAdapter<String> itemsAdapter,
			String message, String id) {
		my.add(id, message);
		itemsAdapter.add(message);
	}
	
}
