package com.example.myandroidapp2.backgroundSerive;

import java.io.FileInputStream;
import java.io.IOException;

import android.os.AsyncTask;

public class ReadStoredChat extends AsyncTask<String,Integer,String> {

	@Override
	protected String doInBackground(String... params) {
		FileInputStream input = null;
		try {
			input= new FileInputStream(params[0]);
			if (input!=null) {
				byte[] buffer = new byte[input.available()];
				if (buffer.length != 0) {
					while (input.read(buffer) >= 0) {}
					return new String(buffer);
				}
			}
		} catch (IOException e) {
			System.out.println("file not exist or reading error "+e.getMessage());
			return null;
		}finally{
			try {
				if (input!=null) {input.close();}
			} catch (IOException e) {
				System.out.println("file close error "+e.getMessage());
			}
		}
		return null;
	}


}
