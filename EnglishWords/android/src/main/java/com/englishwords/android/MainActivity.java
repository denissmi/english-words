package com.englishwords.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.englishwords.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	TTSManager tts = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button english = (Button)findViewById(R.id.button1);
		Button russian = (Button)findViewById(R.id.button2);
		Button add_words = (Button)findViewById(R.id.button3);
		Button show = (Button)findViewById(R.id.button4);
		Button exit = (Button)findViewById(R.id.button5);
		Button settings = (Button)findViewById(R.id.button6);

		english.setOnClickListener(this);
		russian.setOnClickListener(this);
		add_words.setOnClickListener(this);
		show.setOnClickListener(this);
		exit.setOnClickListener(this);
		settings.setOnClickListener(this);

		tts = new TTSManager(this);
	}

	@Override
	public void onClick(View v){
		Intent intentCheckWords = new Intent(MainActivity.this, CheckWords.class);
		switch (v.getId()) {
			case R.id.button1:
				intentCheckWords.putExtra("check", "eng");
				MainActivity.this.startActivity(intentCheckWords);
				break;
			case R.id.button2:
				intentCheckWords.putExtra("check", "rus");
				MainActivity.this.startActivity(intentCheckWords);
				break;
			case R.id.button3:
				Intent intentAddWord = new Intent(MainActivity.this, AddWord.class);
				MainActivity.this.startActivity(intentAddWord);
				break;
			case R.id.button4:
				Intent intentShowWords = new Intent(MainActivity.this, ShowWords.class);
				MainActivity.this.startActivity(intentShowWords);
				break;
			case R.id.button5:
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			case R.id.button6:
				Intent intentLoadDictionary = new Intent(MainActivity.this, LoadDictionary.class);
				MainActivity.this.startActivity(intentLoadDictionary);
				break;
		}
	}

	public void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(),
				message,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tts.shutDown();
	}

}
