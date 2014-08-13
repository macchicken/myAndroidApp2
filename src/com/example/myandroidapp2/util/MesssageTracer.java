package com.example.myandroidapp2.util;

import java.util.LinkedList;

public class MesssageTracer {
	
	private LinkedList<String> messages;
	private static MesssageTracer my= new MesssageTracer();
	
	public MesssageTracer() {
		messages= new LinkedList<String>();
	}
	
	public static MesssageTracer getInstance(){
		return my;
	}
	
	public void add(String id,String message){
		if (messages==null){
			messages= new LinkedList<String>();
		}
		messages.add(id+": "+message);
	}

	public LinkedList<String> getMessages() {
		return messages;
	}

}
