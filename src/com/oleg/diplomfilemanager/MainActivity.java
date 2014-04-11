package com.oleg.diplomfilemanager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	private SystemOverviewFragment systemOverview;
	private SlidingMenu slidingMenu;
	private YandexDiskAuthorization yandexDiskClient;
	private SlidingMenuLayout slidingMenuLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		systemOverview = new SystemOverviewFragment();
		yandexDiskClient = new YandexDiskAuthorization(getApplicationContext());
		Intent intent = getIntent();
		if (intent != null && intent.getData() != null) {
            yandexDiskClient.onLogin(getIntent());
        }
		if (savedInstanceState == null) {
			createSlidingmenu();
			Bundle bundle = new Bundle();
			bundle.putString("displayed_directory",Constants.SD_CARD);
			systemOverview.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, systemOverview).commit();
		}
	}

	private void createSlidingmenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenuLayout=new SlidingMenuLayout(this,slidingMenu, systemOverview);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(slidingMenuLayout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			systemOverview.back();
		}
		return false;
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
