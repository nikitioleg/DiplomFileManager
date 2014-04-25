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

public class SearchResultAdapter extends BaseAdapter {

	private ArrayList<FileInfoItem> fileInfoItems;
	private LayoutInflater lInflater;
	private Context context;

	public SearchResultAdapter(ArrayList<FileInfoItem> fileInfoItems,
			Context context) {
		this.fileInfoItems = fileInfoItems;
		lInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	static class ViewHolder {
		public ImageView ivIco;
		public TextView tvDirName;
		public TextView tvPath;

	}

	public void setData(ArrayList<FileInfoItem> matches) {
		fileInfoItems = matches;
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
			view = lInflater
					.inflate(R.layout.search_item_result, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.tvDirName = (TextView) view
					.findViewById(R.id.search_tvDirName);
			viewHolder.tvPath = (TextView) view
					.findViewById(R.id.search_tvPath);
			viewHolder.ivIco = (ImageView) view.findViewById(R.id.search_img);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tvDirName.setText(fileInfoItems.get(position)
				.getDisplayName());
		viewHolder.tvPath.setText(fileInfoItems.get(position).getFullPath());
		setFileIcon(fileInfoItems.get(position), viewHolder.ivIco);
		return view;
	}

	private void setFileIcon(FileInfoItem fileInfoItem, ImageView imageView) {

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
			else if (fileExp.equalsIgnoreCase("apk"))
				imageView.setImageResource(R.drawable.apk);
			else
				imageView.setImageResource(R.drawable.file);
		}
	}

}
