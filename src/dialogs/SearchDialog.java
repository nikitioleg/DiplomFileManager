package dialogs;

import com.oleg.diplomfilemanager.FileManagment;
import com.oleg.diplomfilemanager.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class SearchDialog extends DialogFragment implements
		android.view.View.OnClickListener {

	String[] data = { "Все типы", "Аудио", "Видео", "Изображения", "Текст",
			"APK", "Zip" };

	String[] trueData = { "/", "audio/", "video/", "image/", "text/",
			"application/vnd.android.package-archive", "application/zip" };

	private static String currentDir;
	private EditText etSearch;
	private Button butSearch;
	private RadioGroup rgSearchIn;
	private Spinner spinnerFileType;
	private CheckBox cbNotCaseSensitive, cbSubfolder;

	public static DialogFragment getInstanse(String current) {
		currentDir = current;
		return new SearchDialog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Создать");
		View view = inflater.inflate(R.layout.search, null);
		etSearch = (EditText) view.findViewById(R.id.etSearchField);
		rgSearchIn = (RadioGroup) view.findViewById(R.id.rgSearchIn);
		cbNotCaseSensitive = (CheckBox) view
				.findViewById(R.id.cbNotCaseSensitive);
		cbSubfolder = (CheckBox) view.findViewById(R.id.cdAllFolders);
		butSearch = (Button) view.findViewById(R.id.butSearch);
		butSearch.setOnClickListener(this);
		spinnerFileType = (Spinner) view.findViewById(R.id.spinnerTypes);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, data);
		spinnerFileType.setAdapter(adapter);
		return view;
	}

	@Override
	public void onClick(View v) {

		Bundle bundle = new Bundle();
		bundle.putString("key_word", etSearch.getText().toString());
		if (rgSearchIn.getCheckedRadioButtonId() == R.id.currentDir)
			bundle.putBoolean("current_dir", true);
		else {
			bundle.putBoolean("current_dir", false);
		}
		if (cbNotCaseSensitive.isChecked()) {
			bundle.putBoolean("lower_case", true);
		} else {
			bundle.putBoolean("lower_case", false);
		}
		if (cbSubfolder.isChecked()) {
			bundle.putBoolean("subfolder", true);
		} else {
			bundle.putBoolean("subfolder", false);
		}

		bundle.putString("searchFileType",
				trueData[spinnerFileType.getSelectedItemPosition()]);

		Log.d("myLogs", "spinner "
				+ spinnerFileType.getSelectedItem().toString());
		FileManagment.getInstance().searchFile(bundle, currentDir);
	}

}
