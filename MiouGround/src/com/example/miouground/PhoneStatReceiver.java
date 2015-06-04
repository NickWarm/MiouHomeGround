package com.example.miouground;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

// 自動啟動Activity: http://blog.csdn.net/rhljiayou/article/details/7103921

public class PhoneStatReceiver extends BroadcastReceiver {
	String TAG = "tag";
	String TAG2 = "tag2";
	TelephonyManager telMgr;

	// SharedPreferences、editor的學習
	// 出處: http://blog.csdn.net/renero/article/details/6635728
	// 如果想要編輯SharedPreferences中的內容就需要用到editor
	

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		telMgr = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		switch (telMgr.getCallState()) {
		case TelephonyManager.CALL_STATE_RINGING: // 電話監聽代碼

			// String number = intent.getStringExtra(Service.TELEPHONY_SERVICE);
			// // 經過測試為null
			// 測試 TelephonyManager.EXTRA_INCOMING_NUMBER
			String number = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER); // 經過測試這才是正確的寫法，出處:
																				// http://stephen830.iteye.com/blog/1181010

			Log.v(TAG, "number:" + number);

			// 偏好設定(SharedPreferences)提供一個簡易的方式來儲存應用程式的設定值，
			// 方便下次應用程式被啟動時，載入偏好設定，讓應用程式自動回復到前一次設定值。

			// String s = ContactsContract.CommonDataKinds.Phone.NUMBER;
			// 判斷來電號碼是否存在手機裡

			/*
			 * // 測試拿掉這個 if (number == "032654628") // 原
			 * if(!getPhoneNum(context).contains(number)) {
			 * 
			 * String takecare =intent.getStringExtra("HomeGaund is alarming ");
			 *  //if(number ==
			 * ){} Log.v(TAG, "take care:" + takecare);
			 * 
			 * SharedPreferences phonenumSP = context.getSharedPreferences(
			 * "in_phone_num", Context.MODE_PRIVATE); Editor editor =
			 * phonenumSP.edit(); editor.putString(number, number);
			 * editor.commit(); endCall();
			 * 
			 * }
			 */
			
			Log.v(TAG2, "take care");
			if (number.equals ("032654628")) {
				//Log.v(TAG2, "take care");
				Intent myIntent = new Intent(context, MainActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(myIntent);
				Log.i("TAG", "activity啟動完畢");
			}

			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			break;
		}
	}

	/**
	 * 挂断电话
	 */
	private void endCall() {
		Class<TelephonyManager> c = TelephonyManager.class;
		try {
			Method getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
			ITelephony iTelephony = null;
			Log.e(TAG, "End call.");
			iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr,
					(Object[]) null);
			iTelephony.endCall();
		} catch (Exception e) {
			Log.e(TAG, "Fail to answer ring call.", e);
		}
	}

	private ArrayList<String> getPhoneNum(Context context) {
		ArrayList<String> numList = new ArrayList<String>();
		// 得到ContentResolver對象
		ContentResolver cr = context.getContentResolver();
		// 取得电话本中开始一项的光标
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		while (cursor.moveToNext()) {
			// 取得联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phone = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
							+ contactId, null, null);
			// 取得手機裡已存的電話號碼 (可存多個號碼)
			while (phone.moveToNext()) {
				String strPhoneNumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				numList.add(strPhoneNumber);
				Log.v("tag", "strPhoneNumber:" + strPhoneNumber);

			}

			phone.close();
		}

		cursor.close();
		return numList;
	}
}
