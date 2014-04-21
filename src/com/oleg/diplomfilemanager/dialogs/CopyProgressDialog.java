package com.oleg.diplomfilemanager.dialogs;

import java.io.File;

import com.oleg.diplomfilemanager.R;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

import android.R.anim;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CopyProgressDialog extends DialogFragment implements
		android.view.View.OnClickListener {

	private static SystemOverviewFragment overviewFragment;
	private TextView tvNameCipyFile, tvAllProgress;
	private ImageView ivIcon;
	private ProgressBar fileProgress, allProgress;
	private TextView tvFileProgress;
	private Button butCansel;
	private int copied = 1;
	private static int amount = 0;

	public static CopyProgressDialog getInstance(
			SystemOverviewFragment systemOverviewFragment, String inFullPath) {
		overviewFragment = systemOverviewFragment;
		amount = getFilesForCopy(inFullPath);
		CopyProgressDialog progressDialog = new CopyProgressDialog();
		return progressDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setCancelable(false);
		getDialog().setTitle("Копирование");
		View view = inflater.inflate(R.layout.copy_dialog_progress, null);
		tvNameCipyFile = (TextView) view.findViewById(R.id.name_copy_file);
		ivIcon = (ImageView) view.findViewById(R.id.copy_item_icon);
		fileProgress = (ProgressBar) view.findViewById(R.id.file_progress);
		fileProgress.setMax(100);
		tvFileProgress = (TextView) view.findViewById(R.id.tv_file_progress);
		allProgress = (ProgressBar) view.findViewById(R.id.all_progress);
		tvAllProgress = (TextView) view.findViewById(R.id.tv_all_progress);
		butCansel = (Button) view.findViewById(R.id.but_cansel);
		butCansel.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}

	public void updateProgress(final String inFullPath, final int progress) {
		overviewFragment.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				enhanceProgress(inFullPath, progress);
			}
		});
	}

	public void enhanceProgress(String inFullPath, int progress) {

		tvNameCipyFile.setText(inFullPath);
		// Adapter.setFileIcon(file, ivIcon);
		if (amount == 1) {
			fileProgress.setProgress(progress);
			tvFileProgress.setText(progress + " %");
			allProgress.setProgress(progress);
			tvAllProgress.setText(progress + " %");
			progress++;
		} else {
			fileProgress.setProgress(progress);
			tvFileProgress.setText(progress + " %");
			allProgress.setMax(amount);
			allProgress.setProgress(copied);
			tvAllProgress.setText(copied + " / " + amount);
			if (progress == 100) {
				progress = 0;
				copied++;
			}
			if (copied == amount) {
				this.dismiss();
			}
		}

	}

	private static int getFilesForCopy(String copyPath) {
		int amount = 0;
		File file = new File(copyPath);
		if (file.isDirectory()) {
			for (File target : file.listFiles()) {
				if (!target.isDirectory())
					amount++;
				else
					getFilesForCopy(target.getAbsolutePath());
			}
			return amount;
		}
		return 1;
	}

}
