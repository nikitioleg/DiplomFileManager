package com.oleg.diplomfilemanager;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oleg.diplomfilemanager.adapters.SlidingMenuAdapter;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import com.oleg.diplomfilemanager.fragments.YandexDiskOverviewFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
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
		yandexDiskAuthorization = new YandexDiskAuthorization(ctx);
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
		if (groupPosition == 0) {
			if (childPosition <= FileManagment.getInstance().getPhoneStorages()
					.size()-1) {
				((ActionBarActivity) context)
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.container,
								onCreateOverviewFragment(
										FileManagment.getInstance()
												.getPhoneStorages()
												.get(childPosition)
												.getAbsolutePath(),
										Constants.SD_CARD_STORAGE)).commit();
			}
			if (childPosition == (FileManagment.getInstance()
					.getPhoneStorages().size())) {
				if (yandexDiskAuthorization.startYandexDiskAuthorization()) {
					((ActionBarActivity) context)
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(
									R.id.container,
									onCreateOverviewFragment(Constants.ROOT,
											Constants.YANDEX_DISK_STORAGE))
							.commit();
				}
			}
		}
		slidingMenu.toggle();
		return false;

		// if (groupPosition == 0 & childPosition == 2) {
		// if (yandexDiskAuthorization.startYandexDiskAuthorization()) {
		// ((ActionBarActivity) context)
		// .getSupportFragmentManager()
		// .beginTransaction()
		// .replace(
		// R.id.container,
		// onCreateOverviewFragment(Constants.ROOT,
		// Constants.YANDEX_DISK_STORAGE))
		// .commit();
		// slidingMenu.toggle();
		// return false;
		// }
		// }
		// return false;
	}

	private ListFragment onCreateOverviewFragment(String dir, int storage) {
		ListFragment fragment;
		FileManagment.getInstance().setCerrentStorage(storage);
		FileManagment.getInstance().setCurrentDir(dir);
		clearBackStack();
		if (storage == Constants.SD_CARD_STORAGE) {
			fragment = new SystemOverviewFragment();
		} else {
			fragment = new YandexDiskOverviewFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putString(Constants.DISP_DIR, dir);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void clearBackStack() {

		Runnable r = new Runnable() {

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
