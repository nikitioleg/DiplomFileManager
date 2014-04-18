package dialogs;

import java.io.File;

import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.R;
import com.oleg.diplomfilemanager.fragments.SystemOverviewFragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NewFileDirDialog extends DialogFragment implements
		android.view.View.OnClickListener {

	private RadioGroup radioGroup;
	private RadioButton radioButtonFile, radioButtonDir;
	private EditText editTextName;
	private Button buttonOk, buttonCancel;
	private static String currentDir;
	private boolean isDir = true;
	static SystemOverviewFragment systemOverviewFragment;

	public static DialogFragment getInstance(
			SystemOverviewFragment overviewFragment, String string) {
		systemOverviewFragment = overviewFragment;
		currentDir = string;
		return new NewFileDirDialog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Создать");
		View view = inflater.inflate(R.layout.create_file_or_dir, null);
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
		radioButtonDir = (RadioButton) view.findViewById(R.id.radio_dir);
		radioButtonDir.setOnClickListener(this);
		radioButtonFile = (RadioButton) view.findViewById(R.id.radio_file);
		radioButtonFile.setOnClickListener(this);
		editTextName = (EditText) view.findViewById(R.id.etName);
		buttonOk = (Button) view.findViewById(R.id.but_ok);
		buttonCancel = (Button) view.findViewById(R.id.but_cansel);
		buttonOk.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio_dir:
			isDir = true;
			break;
		case R.id.but_ok:
			if (FileManagment.getInstance().createFileOrDir(newFile(), isDir)) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						systemOverviewFragment.updateList(FileManagment
								.getInstance().getList(currentDir));
					}
				});
			}
			dismiss();

			break;
		case R.id.radio_file:
			isDir = false;
			break;
		case R.id.but_cansel:
			dismiss();
			break;
		}
	}

	private File newFile() {
		String newName = editTextName.getText().toString();
		if (newName == null || newName == "") {
			Toast.makeText(getActivity(), "Неверное имя файла",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		File newFile = new File(currentDir + "/" + newName);
		if (newFile.exists()) {
			Toast.makeText(getActivity(), "Файл уже существует",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		return newFile;
	}
}
