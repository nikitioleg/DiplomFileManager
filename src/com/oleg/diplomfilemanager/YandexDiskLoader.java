package com.oleg.diplomfilemanager;

import java.io.IOException;
import java.util.ArrayList;

import com.yandex.disk.client.Credentials;
import com.yandex.disk.client.ListItem;
import com.yandex.disk.client.ListParsingHandler;
import com.yandex.disk.client.TransportClient;
import com.yandex.disk.client.exceptions.CancelledPropfindException;
import com.yandex.disk.client.exceptions.WebdavException;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class YandexDiskLoader extends AsyncTaskLoader<ArrayList<FileInfoItem>> {

	private Credentials credentials;
	private Context context;
	private TransportClient transportClient;
	private String currentDir;
	private boolean hasCancelled;
	private FileManagment fileManagment;
	private Handler handler;

	public YandexDiskLoader(Context context,
			com.yandex.disk.client.Credentials credentials, String currentDir) {
		super(context);
		this.credentials = credentials;
		this.context = context;
		this.currentDir = currentDir;
		handler = new Handler();
		fileManagment = FileManagment.getInstance();
	}

	@Override
	protected void onStartLoading() {

		Log.d("myLog", "onStartLoading");

		forceLoad();// run loader

	}

	@Override
	protected void onReset() {
		super.onReset();
		hasCancelled = true;
	}

	@Override
	public ArrayList<FileInfoItem> loadInBackground() {

		Log.d("myLog", "loadInBackground");

		final ArrayList<FileInfoItem> fileInfoItems = new ArrayList<FileInfoItem>();
		transportClient = null;
		hasCancelled = false;
		try {
			transportClient = TransportClient.getInstance(context, credentials);
			transportClient.getList(currentDir, new ListParsingHandler() {

				boolean firstItem = true;
				boolean ignoreFirstItem = true;

				@Override
				public boolean hasCancelled() {

					return hasCancelled;
				}

				@Override
				public void onPageFinished(int itemsOnPage) {
					firstItem = true;
					ignoreFirstItem = true;
					handler.post(new Runnable() {

						@Override
						public void run() {
							deliverResult(fileInfoItems);
							Log.d("myLog", "deliverResult");
						}
					});
				}

				@Override
				public boolean handleItem(ListItem item) {
					if (currentDir.equalsIgnoreCase("/") & ignoreFirstItem) {
						firstItem = false;
						ignoreFirstItem = false;
						return false;
					} else {
						fileInfoItems.add(toFileInfoItem(item, firstItem));
						firstItem = false;
						return true;
					}

				}
			});
		} catch (CancelledPropfindException ex) {
			return fileInfoItems;
		} catch (WebdavException ex) {
			Log.d("f", "loadInBackground", ex);
		} catch (IOException ex) {
			Log.d("f", "loadInBackground", ex);
		} finally {
			TransportClient.shutdown(transportClient);
		}
		return fileInfoItems;
	}

	public FileInfoItem toFileInfoItem(ListItem listItem, boolean firstItem) {
		FileInfoItem.Builder builder;
		builder = new FileInfoItem.Builder();
		if (firstItem) {
			String fullPath = getParent(listItem.getFullPath());
			builder.setFullPath(fullPath);
			builder.setDisplayName(" ... ");
			builder.addCollection(true);
			builder.addPreviousFolder(true);
			return builder.build();
		}else{
			builder.setFullPath(listItem.getFullPath());
		builder.setDisplayName(listItem.getDisplayName());
		builder.setContentLength(fileManagment.getFileSizeToString(listItem
				.getContentLength()));
		builder.setLastModified(fileManagment.getLastModif(listItem
				.getLastUpdated()));
		builder.setContentType(listItem.getContentType());
		builder.addCollection(listItem.isCollection());
		builder.addReadable(false);
		builder.addWritable(false);
		builder.setVisible(listItem.isVisible());
		builder.setPublicUrl(listItem.getPublicUrl());
		builder.addPreviousFolder(false);
		return builder.build();
		}
		
	}

	private String getParent(String fullPath) {
		int slashPos = fullPath.lastIndexOf("/");
		if (slashPos == 0) {
			return new String("/");
		}
		return new String(fullPath.substring(0, slashPos));
	}

}
