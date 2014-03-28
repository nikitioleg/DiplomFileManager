package com.oleg.diplomfilemanager.adapters;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SystemOverviewAdapter extends BaseAdapter {

	private LayoutInflater lInflater;
	private Context context;
	private ArrayList<FileInfoItem> fileInfoItems;

	public SystemOverviewAdapter(Context context,
			ArrayList<FileInfoItem> fileInfoItems) {
		this.context = context;
		this.fileInfoItems = fileInfoItems;
		lInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

		View view = convertView;
		if (convertView == null) {
			view = lInflater.inflate(R.layout.system_overview_item, parent,
					false);
		}
		TextView tvDirName = (TextView) view.findViewById(R.id.tvDirName);
		TextView tvLastEdit = (TextView) view.findViewById(R.id.tvLastEdit);
		TextView tvFileSize = (TextView) view.findViewById(R.id.tvFileSize);
		tvDirName.setText(fileInfoItems.get(position).getFullPath());
		tvLastEdit.setText(fileInfoItems.get(position).getLastModified());
		tvFileSize.setText(fileInfoItems.get(position).getContentLength()
				+ fileInfoItems.get(position).getFilePermissions());

		return view;
	}

}
