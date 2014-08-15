package com.example.myandroidapp2.backgroundSerive;

import java.util.LinkedList;

import com.example.myandroidapp2.util.CommonUtils;

import dto.Person;
import android.content.Context;
import android.os.AsyncTask;

public class ReadPhoneContacts extends AsyncTask<Context,Integer,String> {

	@Override
	protected String doInBackground(Context... params) {
		LinkedList<Person> contacts = CommonUtils.getPhoneContacts(params[0]);
		if (contacts!=null&&contacts.size()!=0) {
			String temp = contacts.toString();
			temp = temp.replaceAll(", ", "");
			temp = temp.replace("[", "");
			temp = temp.replace("]", "");
			return temp;
		}
		return null;
	}

}
