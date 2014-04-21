package com.oleg.diplomfilemanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.oleg.diplomfilemanager.Constants;
import com.oleg.diplomfilemanager.FileInfoItem;
import com.oleg.diplomfilemanager.LongFileOperatoin;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;

public class DeleteDialog extends DialogFragment {

	static SystemOverviewFragment overviewFragment;
	static String currentDir;

	public static DeleteDialog getInstance(FileInfoItem fileInfoItem,
			String string, SystemOverviewFragment systemOverviewFragment) {
		overviewFragment = systemOverviewFragment;
		currentDir = string;
		Bundle bundle = new Bundle();
		bundle.putParcelable("file_info_item", fileInfoItem);
		DeleteDialog deleteDialog = new DeleteDialog();
		deleteDialog.setArguments(bundle);
		return deleteDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity()).setTitle("Удаление")
				.setMessage("Удалить файл?")
				.setPositiveButton("Да", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bundle bundle = new Bundle();
						bundle = getArguments();
						FileInfoItem fileInfoItem = bundle
								.getParcelable("file_info_item");
											LongFileOperatoin operatoin = new LongFileOperatoin(
								Constants.PHONE_STORAGE_DELETE,fileInfoItem , currentDir,
								overviewFragment);
						operatoin.start();
					}
				}).setNegativeButton("Нет", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				}).create();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}
}
