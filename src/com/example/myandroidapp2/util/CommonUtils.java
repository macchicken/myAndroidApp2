package com.example.myandroidapp2.util;

import java.util.LinkedList;

import dto.Person;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CommonUtils {
	
	private static MesssageTracer my = MesssageTracer.getInstance();
	private static final String[] PHONES_PROJECTION = new String[] {
        Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };

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
		itemsAdapter.add(id+": "+message);
	}
	
	/**access the contacts list in the phone database**/
	public static LinkedList<Person> getPhoneContacts(Context mContext) {
		ContentResolver resolver = mContext.getContentResolver();

		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			LinkedList<Person> contacts=new LinkedList<Person>();
			Person temp;
			while (phoneCursor.moveToNext()) {
				String phoneNumber = phoneCursor.getString(1);
				if (phoneNumber==null||"".equals(phoneNumber.trim()))
					continue;
				temp=new Person();
				phoneNumber=phoneNumber.replaceAll(" ", "");
				phoneNumber=phoneNumber.replaceAll("-", "");
				String contactName = phoneCursor.getString(0);
				Long contactId = phoneCursor.getLong(3);
				temp.setPhoneNo(phoneNumber);
				temp.setName(contactName);
				temp.setContactId(contactId.intValue());
				contacts.add(temp);
			}
			phoneCursor.close();
			return contacts;
		}
		return null;
	}

}
