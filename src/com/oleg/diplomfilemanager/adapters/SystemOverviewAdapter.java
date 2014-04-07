package com.oleg.diplomfilemanager.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.spec.IvParameterSpec;

import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemOverviewAdapter extends BaseAdapter {

	private LayoutInflater lInflater;
	private static Context context;
	private ArrayList<FileInfoItem> fileInfoItems;
	private SharedPreferences sharedPreferences;

	public SystemOverviewAdapter(Context context,
			ArrayList<FileInfoItem> fileInfoItems) {
		this.context = context;
		this.fileInfoItems = fileInfoItems;
		lInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

	//	getStorage();
	}

	static class ViewHolder {
		public ImageView ivIco;
		public TextView tvDirName;
		public TextView tvLastEdit;
		public TextView tvFileSize;
	}

	@Override
	public int getCount() {
		return fileInfoItems.size();
	}

	@Override
	public Object getItem(int position) {
		return fileInfoItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View view = convertView;
		if (convertView == null) {
			view = lInflater.inflate(R.layout.system_overview_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.tvDirName = (TextView) view.findViewById(R.id.tvDirName);
			viewHolder.tvLastEdit = (TextView) view
					.findViewById(R.id.tvLastEdit);
			viewHolder.tvFileSize = (TextView) view
					.findViewById(R.id.tvFileSize);
			viewHolder.ivIco = (ImageView) view.findViewById(R.id.ivIco);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tvDirName.setText(fileInfoItems.get(position).getName());
		viewHolder.tvLastEdit.setText(fileInfoItems.get(position)
				.getLastModified());
		viewHolder.tvFileSize.setText(fileInfoItems.get(position)
				.getContentLength()
				+ fileInfoItems.get(position).getFilePermissions());
		setFileIcon(fileInfoItems.get(position), viewHolder.ivIco);
		return view;
	}

	public static void setFileIcon(final FileInfoItem fileInfoItem,
			ImageView imageView) {

		if (fileInfoItem.isPreviousFolder()) {
			imageView.setImageResource(R.drawable.folder_up);
			return;
		}

		if (fileInfoItem.isCollection()) {
			imageView.setImageResource(R.drawable.folder);
		} else {

			String fileExp = fileInfoItem.getFullPath().substring(
					fileInfoItem.getFullPath().lastIndexOf(".") + 1);

			if (fileExp.equalsIgnoreCase("mp3"))
				imageView.setImageResource(R.drawable.mp3);
			else if (fileExp.equalsIgnoreCase("jpg"))
				imageView.setImageResource(R.drawable.jpg);
			else if (fileExp.equalsIgnoreCase("bmp"))
				imageView.setImageResource(R.drawable.bmp);
			else if (fileExp.equalsIgnoreCase("png"))
				imageView.setImageResource(R.drawable.png);
			else if (fileExp.equalsIgnoreCase("gif"))
				imageView.setImageResource(R.drawable.gif);
			else if (fileExp.equalsIgnoreCase("djvu"))
				imageView.setImageResource(R.drawable.djvu);
			else if (fileExp.equalsIgnoreCase("doc"))
				imageView.setImageResource(R.drawable.doc);
			else if (fileExp.equalsIgnoreCase("docx"))
				imageView.setImageResource(R.drawable.docx);
			else if (fileExp.equalsIgnoreCase("dwf"))
				imageView.setImageResource(R.drawable.dwf);
			else if (fileExp.equalsIgnoreCase("pdf"))
				imageView.setImageResource(R.drawable.pdf);
			else if (fileExp.equalsIgnoreCase("zip"))
				imageView.setImageResource(R.drawable.zip);
			else if (fileExp.equalsIgnoreCase("rar"))
				imageView.setImageResource(R.drawable.rar);
			else if (fileExp.equalsIgnoreCase("rtf"))
				imageView.setImageResource(R.drawable.rtf);
			else if (fileExp.equalsIgnoreCase("xml"))
				imageView.setImageResource(R.drawable.xml);
			else if (fileExp.equalsIgnoreCase("apk")) {

				ExecutorService ex = Executors.newCachedThreadPool();
				Future<Bitmap> icon = ex.submit(new Callable<Bitmap>() {

					@Override
					public Bitmap call() throws Exception {
						return getApkIcon(fileInfoItem.getFullPath());
					}
				});

				try {
					if (icon.get() == null)
						imageView.setImageResource(R.drawable.apk);
					else
						imageView.setImageBitmap(icon.get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			} else
				imageView.setImageResource(R.drawable.file);
		}
	}

	private static Bitmap getApkIcon(String fullPath) {
		Bitmap bmpIcon = null;

		PackageInfo packageInfo = context.getPackageManager()
				.getPackageArchiveInfo(fullPath, 0);

		if (packageInfo != null) {
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			if (Build.VERSION.SDK_INT >= 8) {
				appInfo.sourceDir = fullPath;
				appInfo.publicSourceDir = fullPath;
			}

			Drawable icon = appInfo.loadIcon(context.getPackageManager());
			bmpIcon = ((BitmapDrawable) icon).getBitmap();

		}

		return bmpIcon;
	}

	private ArrayList<String> getStorage() {
		return null;
		
	}

	public ArrayList<String> prepareStorage() {
		String storage = "/storage";
		File temp = new File(storage);
		ArrayList<String> storages = new ArrayList<String>();
		if (temp.exists()) {
			String[] strings = temp.list();
			for (int i = 0; i < strings.length; i++) {
				storages.add(storage + "/" + strings[i]);
			}
			storages.add("Созд. Новое Хранилище");
		} else {
			storages.add(Environment.getExternalStorageDirectory().getPath());
		}
		return storages;
	}

}