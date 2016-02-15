package com.englishwords.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.englishwords.R;

import java.util.List;

public class AddWord extends MainActivity {

	private String englishWd;
	private String russianWd;
	private AlertDialog.Builder ad;
	private WordsDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_word);

		Button add = (Button)findViewById(R.id.button1);
		final EditText englishWord = (EditText)findViewById(R.id.editText1);
		final EditText russianWord = (EditText)findViewById(R.id.editText2);

		datasource = new WordsDataSource(this);
		datasource.open();

		ad = new AlertDialog.Builder(AddWord.this);
		ad.setTitle("Подтверждение");
		ad.setPositiveButton("Подтвердить", new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				if(!existWord()) {
					if(!russianWd.isEmpty() && !englishWd.isEmpty()) {
						long minLevel = datasource.getMinLevel();
						if (minLevel == 0) {
							datasource.createWord(englishWd, russianWd, 0, 1);
						}
						else {
							datasource.createWord(englishWd, russianWd, minLevel-1, 1);
						}
						showToast("Слово добавлено!");
					}
					else {
						showToast("Недостаточный ввод!");
					}
				}
				else {
					showToast("Слово существует!");
				}
				englishWord.setText("");
				russianWord.setText("");
			}

		});
		ad.setNegativeButton("Отменить", new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
			}
		});

		add.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						englishWd = englishWord.getText().toString();
						russianWd = russianWord.getText().toString();

						ad.setMessage("\'" + englishWd + "\' - \'" + russianWd + "\'");
						ad.show();
					}
				});

	}

	private boolean existWord() {
		List<Word> values = datasource.getAllWords();
		boolean existWord = false;
		for(int i=0; i<values.size(); ++i) {
			if(russianWd.equals(values.get(i).getRussian()) || englishWd.equals(values.get(i).getEnglish())) {
				existWord = true;
				break;
			}
		}
		return existWord;
	}

	@Override
	public void onBackPressed()
	{
		datasource.close();
		finish();
	}

}
