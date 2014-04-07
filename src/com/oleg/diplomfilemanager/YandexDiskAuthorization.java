package com.oleg.diplomfilemanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class YandexDiskAuthorization {

	private static final int GET_ACCOUNT_CREDS_INTENT = 100;

	public static final String CLIENT_ID = "36c90d0661b644998b7d36afcee167c5";
	public static final String CLIENT_SECRET = "45693810a541466e9dbb559194c0eed2";

	public static final String ACCOUNT_TYPE = "com.yandex";
	public static final String AUTH_URL = "https://oauth.yandex.ru/authorize?response_type=token&client_id="
			+ CLIENT_ID;
	private static final String ACTION_ADD_ACCOUNT = "com.yandex.intent.ADD_ACCOUNT";
	private static final String KEY_CLIENT_SECRET = "clientSecret";
	public static String USERNAME = "example.username";
	public static String TOKEN = "example.token";

	private Context context;

	public YandexDiskAuthorization(Context context) {
		this.context = context;
	}

	public void startYandexDisKAuthorization() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String token = preferences.getString(TOKEN, null);
		if (token == null) {
			getToken();
			return;
		}
	}

	public void getYndexDiskFileList() {

	}

	public void openYandexDiskFile() {

	}

	public void deleteYandexDiskFile() {

	}

	public void renameYandexDiskFile() {

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
				.parse(AUTH_URL)));

	}

	public void onLogin(Intent intent) {
		Uri data = intent.getData();
		intent=null;
		Pattern pattern = Pattern.compile("access_token=(.*?)(&|$)");
		Matcher matcher = pattern.matcher(data.toString());
		if (matcher.find()) {
			final String token = matcher.group(1);
			if (!TextUtils.isEmpty(token)) {
			//	Log.d(TAG, "onLogin: token: " + token);
				saveToken(token);
			} else {
			//	Log.w(TAG, "onRegistrationSuccess: empty token");
			}
		} else {
		//	Log.w(TAG, "onRegistrationSuccess: token not found in return url");
		}
	}

	private void saveToken(String token) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putString(USERNAME, "");
		editor.putString(TOKEN, token);
		editor.commit();
	}
}
