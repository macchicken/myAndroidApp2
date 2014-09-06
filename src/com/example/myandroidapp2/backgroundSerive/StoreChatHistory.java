package com.example.myandroidapp2.backgroundSerive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import android.os.AsyncTask;

import com.example.myandroidapp2.util.MesssageTracer;

public class StoreChatHistory extends AsyncTask<FileOutputStream,Integer,Boolean> {
	

	@Override
	protected Boolean doInBackground(FileOutputStream... params) {
		try {
			MesssageTracer my=MesssageTracer.getInstance();
			LinkedList<String> messages=my.getMessages();
			if (messages.size()!=0) {
				StringBuilder temp=new StringBuilder();
				for (String message : messages) {temp.append(message + "\n");}
				params[0].write(temp.toString().getBytes());
				my.clear();// clear local memory storage
//				int index=my.getIndex();
//				my.setIndex(index+messages.size());
			}
		} catch (IOException e) {
			return false;
		}finally{
			try {
				if (params[0]!=null) {params[0].close();}
			} catch (IOException e) {
				System.out.println("close internal output file error "+e.getMessage());
			}
		}
		return true;
	}


}
