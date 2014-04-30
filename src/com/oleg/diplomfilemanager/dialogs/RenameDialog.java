package com.oleg.diplomfilemanager.dialogs;

import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.LoadersControl;
import com.oleg.diplomfilemanager.LongFileOperatoin;
import com.oleg.diplomfilemanager.YandexDiskManagment;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

public class RenameDialog extends DialogFragment {

	private static SystemOverviewFragment overviewFragment;
	private static boolean renameMark;
	private static boolean isFile;
	private static String currentPath;

	public static RenameDialog getInstance(FileInfoItem fileInfoItem,
			boolean rename, boolean fileOrDirMark,
			SystemOverviewFragment systemOverviewFragment, String path) {
		renameMark = rename;
		isFile = fileOrDirMark;
		overviewFragment = systemOverviewFragment;
		currentPath = path;
		Bundle bundle = new Bundle();
		bundle.putParcelable("file_info_item", fileInfoItem);
		RenameDialog renameDialog = new RenameDialog();
		renameDialog.setArguments(bundle);
		return renameDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title;
		if (renameMark) {
			title = "Переименовать";
		} else {
			if (isFile) {
				title = "Новый файл";
			} else {
				title = "Новая папка";
			}
		}
		Bundle bundle = new Bundle();
		bundle = getArguments();
		final FileInfoItem fileInfoItem = bundle
				.getParcelable("file_info_item");
		final EditText newDirName = new EditText(getActivity());
		newDirName.setTextColor(Color.BLACK);
		newDirName.setId(1);
		newDirName.setText(fileInfoItem.getDisplayName());

		return new AlertDialog.Builder(getActivity()).setTitle(title)
				.setMessage("Имя").setView(newDirName)
				.setPositiveButton("Да", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						int currentStorage = FileManagment.getInstance()
								.getCurrentStorage();
						switch (currentStorage) {
						case Constants.SD_CARD_STORAGE:
							FileManagment.getInstance().rename(
									fileInfoItem.getFullPath(),
									newDirName.getText().toString());
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									overviewFragment
											.updateList(FileManagment
													.getInstance().getList(
															currentPath));
								}
							});
							break;
						case Constants.YANDEX_DISK_STORAGE:
							new Thread(new Runnable() {
								@Override
								public void run() {
									YandexDiskManagment
											.getInstance()
											.moveYandexDiskFile(
													fileInfoItem.getFullPath(),
													FileManagment.getInstance()
															.getCurrentDir()
															+ newDirName
																	.getText()
																	.toString());
									overviewFragment.getActivity()
											.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													((ActionBarActivity) overviewFragment
															.getActivity())
															.getSupportLoaderManager()
															.destroyLoader(
																	Constants.YANDEX_DISK_LOADER);
													((ActionBarActivity) overviewFragment
															.getActivity())
															.getSupportLoaderManager()
															.initLoader(
																	Constants.YANDEX_DISK_LOADER,
																	null,
																	LoadersControl
																			.getInstance());
												}
											});
								}

							}).start();
							break;
						}

					}
				}).setNegativeButton("Нет", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				}).create();
	}
}
