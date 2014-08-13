package com.example.myandroidapp2.util;

import java.util.LinkedList;

import android.os.Handler;
import android.widget.TextView;

public class CommonUtils {
	
	private static MesssageTracer my = MesssageTracer.getInstance();
	
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

}
