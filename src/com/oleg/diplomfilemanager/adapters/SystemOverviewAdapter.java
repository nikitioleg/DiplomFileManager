package com.oleg.diplomfilemanager.adapters;

import java.util.ArrayList;

import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tvDirName.setText(fileInfoItems.get(position).getFullPath());
		viewHolder.tvLastEdit.setText(fileInfoItems.get(position).getLastModified());
		viewHolder.tvFileSize.setText(fileInfoItems.get(position).getContentLength()
				+ fileInfoItems.get(position).getFilePermissions());

		return view;
	}

}
