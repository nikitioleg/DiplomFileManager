package com.oleg.diplomfilemanager;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oleg.diplomfilemanager.adapters.SlidingMenuAdapter;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import com.oleg.diplomfilemanager.fragments.YandexDiskOverviewFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SlidingMenuLayout extends LinearLayout implements
		OnChildClickListener {

	private ExpandableListView list;
	private Context context;
	private SlidingMenu slidingMenu;
	private SystemOverviewFragment systemOverview;
	private LoaderManager.LoaderCallbacks<ArrayList<FileInfoItem>> loadersControl;
	private YandexDiskAuthorization yandexDiskAuthorization;
	private SlidingMenuAdapter adapter;

	public SlidingMenuLayout(Context ctx, SlidingMenu slidingMenu,
			SystemOverviewFragment systemOverview) {
		super(ctx);
		this.context = ctx;
		this.slidingMenu = slidingMenu;
		this.systemOverview = systemOverview;
		adapter = new SlidingMenuAdapter(ctx);
		yandexDiskAuthorization = new YandexDiskAuthorization(ctx);// создавать
																	// во время
																	// обращения
																	// к сервису
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slidingmenu_fragment, this, true);
		setSlidingMenuAdapter();

	}

	private void setSlidingMenuAdapter() {
		list = (ExpandableListView) findViewById(R.id.slidingmenu_view);

		list.setAdapter(adapter);
		list.setOnChildClickListener(this);
	}

	public ExpandableListView getSlidingMenuList() {
		return list;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Toast.makeText(context, " " + groupPosition + " " + childPosition,
				Toast.LENGTH_SHORT).show();
		if (groupPosition == 0 & childPosition == 0) {
			FileManagment.getInstance().setCerrentStorage(
					Constants.SD_CARD_STORAGE);
			systemOverview.updateList(FileManagment.getInstance().getList(
					Constants.SD_CARD));
			slidingMenu.toggle();
			return false;
		}
		if (groupPosition == 0 & childPosition == 1) {
			if (yandexDiskAuthorization.startYandexDiskAuthorization()) {
				FileManagment.getInstance().setCerrentStorage(
						Constants.YANDEX_DISK_STORAGE);
				FileManagment.getInstance().setCurrentDir(Constants.ROOT);
				clearBackStack();
				YandexDiskOverviewFragment fragment = new YandexDiskOverviewFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.DISP_DIR, Constants.ROOT);
				fragment.setArguments(bundle);
				((ActionBarActivity) context).getSupportFragmentManager()
						.beginTransaction().replace(R.id.container, fragment)
						.commit();
				slidingMenu.toggle();
				return false;
			}

		}
		return false;
	}

	private void clearBackStack() {
		
		Runnable r  = new Runnable(){

			@Override
			public void run() {
				FragmentManager fm = ((ActionBarActivity) context)
						.getSupportFragmentManager();
				for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
					fm.popBackStack();
				}
			}
			
		};
		r.run();
		
	}

}
