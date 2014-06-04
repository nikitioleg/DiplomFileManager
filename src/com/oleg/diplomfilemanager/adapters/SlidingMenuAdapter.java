package com.oleg.diplomfilemanager.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SlidingMenuAdapter extends BaseExpandableListAdapter {

	private String[] group = new String[] { "Хранилища"};
	private String[] storage = new String[] { "sdcard0", "sdcard1","Yandex.Disk" };
	private HashMap<String, ArrayList<String>> data;
	private LayoutInflater layoutInflater;

	public SlidingMenuAdapter(Context ctx) {
		data = prepareData();
		layoutInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private HashMap<String, ArrayList<String>> prepareData() {

		HashMap<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
		ArrayList<String> child = new ArrayList<String>();
		
		for (File file : FileManagment.getInstance().getPhoneStorages()) {
			child.add(file.getName());
		}
		child.add("Yandex.Disk");		
		data.put(group[0], child);

		return data;

	}

	@Override
	public int getGroupCount() {
		return group.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<String> r = new ArrayList<String>();
		r = data.get(group[groupPosition]);
		return r.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(group[groupPosition]).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.slidingmenu_sectionview, null);
		}
		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.slidingmenu_section_title);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.slidingmenu_sectionitem, null);
		}
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.slidingmenu_sectionitem_label);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
