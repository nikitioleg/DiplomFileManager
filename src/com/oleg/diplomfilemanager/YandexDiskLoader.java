package com.oleg.diplomfilemanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.ListParsingHandler;
import com.yandex.disk.client.TransportClient;
import com.yandex.disk.client.exceptions.CancelledPropfindException;
import com.yandex.disk.client.exceptions.WebdavException;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;

public class YandexDiskLoader extends AsyncTaskLoader<ArrayList<ListItem>> {

	private com.yandex.disk.client.Credentials credentials;
	private Context context;
	private TransportClient transportClient;
	private String currentDir;
	private Exception exception;
	private ArrayList<ListItem> fileItemList;
	private Handler handler;

	public YandexDiskLoader(Context context,
			com.yandex.disk.client.Credentials credentials, String currentDir) {
		super(context);
		this.credentials = credentials;
		this.context = context;
		this.currentDir = currentDir;
	}

	@Override
	public ArrayList<ListItem> loadInBackground() {
		fileItemList = new ArrayList<ListItem>();
		transportClient = null;
		try {
			transportClient = TransportClient.getInstance(context, credentials);
			transportClient.getList(currentDir, new ListParsingHandler() {

				boolean ignoreFirstItem = true;

				@Override
				public boolean hasCancelled() {
					return super.hasCancelled();
				}

				@Override
				public void onPageFinished(int itemsOnPage) {
					ignoreFirstItem = true;
					handler.post(new Runnable() {

						@Override
						public void run() {
							deliverResult(new ArrayList<ListItem>(fileItemList));

						}
					});
				}

				@Override
				public boolean handleItem(ListItem item) {
					if (ignoreFirstItem) {
						ignoreFirstItem = false;
						return false;
					} else {
						fileItemList.add(item);
						return true;
					}
				}
			});
		} catch (CancelledPropfindException ex) {
			return fileItemList;
		} catch (WebdavException ex) {
			// Log.d(TAG, "loadInBackground", ex);
			exception = ex;
		} catch (IOException ex) {
			// Log.d(TAG, "loadInBackground", ex);
			exception = ex;
		} finally {
			TransportClient.shutdown(transportClient);
		}
		return fileItemList;
	}

}
