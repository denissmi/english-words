package com.englishwords;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ShowWords extends MainActivity {

	private WordsDataSource datasource;
	private AlertDialog.Builder adDelete;
	Word wdDelete, wdUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_words);

		datasource = new WordsDataSource(this);
		datasource.open();

		final ListView lv = (ListView)findViewById(R.id.listView1);
		final Button sort = (Button)findViewById(R.id.button1);

		adDelete = new AlertDialog.Builder(ShowWords.this);
		adDelete.setTitle("Удаление");
		adDelete.setPositiveButton("Удалить", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				datasource.deleteWord(wdDelete);
				String textButton = sort.getText().toString();
				if(textButton.equals(getString(R.string.addition))) {
					lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getSortEngWords()));
				}
				else if(textButton.equals(getString(R.string.alphabet))){
					lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getAllWords()));
				}
			}
		});
		adDelete.setNegativeButton("Отменить", new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
			}
		});

		sort.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						String textButton = sort.getText().toString();
						if(textButton.equals(getString(R.string.alphabet))) {
							lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getSortEngWords()));
							sort.setText(R.string.addition);
						}
						else if(textButton.equals(getString(R.string.addition))){
							lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getAllWords()));
							sort.setText(R.string.alphabet);
						}
					}
				});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
				Object obj = lv.getItemAtPosition(position);
				wdDelete = (Word)obj;
				adDelete.setMessage("\'" + wdDelete.getEnglish() + "\' - \'" + wdDelete.getRussian() + "\'");
				adDelete.show();
				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {

				LayoutInflater li = LayoutInflater.from(ShowWords.this);
				View updateWordView = li.inflate(R.layout.update_word, null);

				AlertDialog.Builder adUpdate = new AlertDialog.Builder(ShowWords.this);
				adUpdate.setTitle("Изменение");

				adUpdate.setView(updateWordView);

				final EditText inputEng = (EditText) updateWordView
						.findViewById(R.id.editText1);
				final EditText inputRus = (EditText) updateWordView
						.findViewById(R.id.editText2);

				Object obj = lv.getItemAtPosition(position);
				wdUpdate = (Word)obj;

				inputEng.setText(wdUpdate.getEnglish());
				inputRus.setText(wdUpdate.getRussian());

				adUpdate.setPositiveButton("Изменить", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						datasource.updateWord(wdUpdate, inputEng.getText().toString(), inputRus.getText().toString());
						String textButton = sort.getText().toString();
						if(textButton.equals(getString(R.string.addition))) {
							lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getSortEngWords()));
						}
						else if(textButton.equals(getString(R.string.alphabet))){
							lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getAllWords()));
						}
					}
				});
				adUpdate.setNegativeButton("Отменить", new OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				});

				adUpdate.show();
			}
		});

		lv.setAdapter(new WordAdapter(ShowWords.this, datasource.getAllWords()));
	}

	@Override
	public void onBackPressed()
	{
		datasource.close();
		finish();
	}
}
