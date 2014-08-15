package com.example.myandroidapp2.util;

import java.util.LinkedList;

public class MesssageTracer {
	
	private LinkedList<String> messages;
	private static MesssageTracer my= new MesssageTracer();
	private int index=0;
	
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
	
	public LinkedList<String> getpartOfMessages(){
		LinkedList<String> subList = new LinkedList<String>();
		int messesgeSize=messages.size();
		for (int i=index;i<messesgeSize;i++){subList.add(messages.get(i));}
		return subList;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
}
