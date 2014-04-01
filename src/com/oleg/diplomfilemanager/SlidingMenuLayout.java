package com.oleg.diplomfilemanager;

import com.oleg.diplomfilemanager.adapters.SlidingMenuAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SlidingMenuLayout extends LinearLayout implements
		OnChildClickListener {

	private ExpandableListView list;
	Context ctx;

	public SlidingMenuLayout(final Context ctx) {
		super(ctx);
		this.ctx = ctx;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slidingmenu_fragment, this, true);
		list = (ExpandableListView) findViewById(R.id.slidingmenu_view);
		SlidingMenuAdapter adapter = new SlidingMenuAdapter(ctx);
		list.setAdapter(adapter);
		list.setOnChildClickListener(this);
	}

	public ExpandableListView getSlidingMenuList() {
		return list;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(ctx, " " + groupPosition + " " + childPosition,
				Toast.LENGTH_SHORT).show();
		return false;
	}
}
