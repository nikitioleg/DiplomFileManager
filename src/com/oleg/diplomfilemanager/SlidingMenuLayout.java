package com.oleg.diplomfilemanager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oleg.diplomfilemanager.adapters.SlidingMenuAdapter;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SlidingMenuLayout extends LinearLayout implements
		OnChildClickListener {

	private final String SD_CARD = Environment.getExternalStorageDirectory()
			.getPath();
	//private final String INTEGRAL_MEMORY = ctx.getFilesDir().getAbsolutePath();

	private ExpandableListView list;
	private Context ctx;
	private SlidingMenu slidingMenu;
	private SystemOverviewFragment systemOverview;

	public SlidingMenuLayout(Context ctx, SlidingMenu slidingMenu,
			SystemOverviewFragment systemOverview) {
		super(ctx);
		this.ctx = ctx;
		this.slidingMenu = slidingMenu;
		this.systemOverview = systemOverview;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slidingmenu_fragment, this, true);
		setSlidingMenuAdapter();

	}

	private void setSlidingMenuAdapter() {
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
		if (groupPosition == 0 & childPosition == 0) {
			systemOverview.updateList(SD_CARD);
			slidingMenu.toggle();
			return false;
		}
		if (groupPosition == 0 & childPosition == 1) {
			String string = ctx.getFilesDir().getAbsolutePath();
			systemOverview.updateList(string);
			slidingMenu.toggle();
			return false;
		}
		return false;
	}
}
