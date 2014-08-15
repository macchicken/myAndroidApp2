package com.example.myandroidapp2.backgroundSerive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import android.os.AsyncTask;

import com.example.myandroidapp2.util.MesssageTracer;

public class StoreChatHistory extends AsyncTask<FileOutputStream,Integer,Boolean> {
	
	private static MesssageTracer my=MesssageTracer.getInstance();

	@Override
	protected Boolean doInBackground(FileOutputStream... params) {
		try {
			LinkedList<String> messages=my.getpartOfMessages();
			if (messages.size()!=0) {
				StringBuilder temp=new StringBuilder();
				for (String message : messages) {temp.append(message + "\n");}
				params[0].write(temp.toString().getBytes());
				int index=my.getIndex();
				my.setIndex(index+messages.size());
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
