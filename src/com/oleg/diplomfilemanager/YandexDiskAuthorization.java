package com.oleg.diplomfilemanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.rtp.RtpStream;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class YandexDiskAuthorization {

	private Context context;

	public YandexDiskAuthorization(Context context) {
		this.context = context;
	}

	public boolean startYandexDiskAuthorization() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String token = preferences.getString(Constants.TOKEN, null);
		if (token == null) {
			getToken();
			return false;
		} else
			return true;
	}

	private void getToken() {
		// AccountManager accountManager = AccountManager
		// .get(context);
		// Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
		// // Log.d(TAG, "accounts: "+(accounts != null ? accounts.length :
		// null));
		//
		// if (accounts != null && accounts.length > 0) {
		// // get the first account, for example (you must show the list and
		// // allow user to choose)
		// Account account = accounts[0];
		// // Log.d(TAG, "account: "+account);
		// getAuthToken(account);
		// return;
		// }
		//
		// Log.d(TAG, "No such accounts: " + ACCOUNT_TYPE);
		// for (AuthenticatorDescription authDesc : accountManager
		// .getAuthenticatorTypes()) {
		// if (ACCOUNT_TYPE.equals(authDesc.type)) {
		// Log.d(TAG, "Starting " + ACTION_ADD_ACCOUNT);
		// Intent intent = new Intent(ACTION_ADD_ACCOUNT);
		// startActivityForResult(intent, GET_ACCOUNT_CREDS_INTENT);
		// return;
		// }
		// }

		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse(Constants.AUTH_URL)));

	}

	public void onLogin(Intent intent) {
		Uri data = intent.getData();
		intent = null;
		Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
		Matcher matcher = pattern.matcher(data.toString());
		if (matcher.find()) {
			final String token = matcher.group(1);
			if (!TextUtils.isEmpty(token)) {
				saveToken(token);
			} 
		} 
	}

	private void saveToken(String token) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putString(Constants.USERNAME, "");
		editor.putString(Constants.TOKEN, token);
		editor.commit();
	}
}
