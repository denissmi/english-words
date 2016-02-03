package com.englishwords;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckWords extends MainActivity {

	private int wordId;
	private WordsDataSource datasource;
	private AlertDialog.Builder adError;
	private AlertDialog.Builder adConfirm;
	private List<Word> valuesBD;
	
	private final int REQ_CODE_SPEECH_INPUT = 100;
	private String resultSpeech = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_words);
		
		Intent intent = getIntent();
		final String language = intent.getStringExtra("check");
		
		final TextView checkWord = (TextView)findViewById(R.id.textView1);
		Button answer = (Button)findViewById(R.id.button1);
		Button dontKnow = (Button)findViewById(R.id.button2);
		Button speech = (Button)findViewById(R.id.button3);
		final Button hideText = (Button)findViewById(R.id.button4);
		final EditText answerWord = (EditText)findViewById(R.id.editText1);
		
		if(language.equals("eng")) {
			speech.setText(R.string.voice);
			speech.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
			            tts.initQueue(valuesBD.get(wordId-1).getEnglish());
					}
				});
			hideText.setVisibility(View.VISIBLE);
			hideText.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(hideText.getText().toString().equals(getString(R.string.hide))) {
						hideText.setText(getString(R.string.show_word));
						checkWord.setText("Воспринимайте на слух!");
						tts.initQueue(valuesBD.get(wordId-1).getEnglish());
					}
					else {
						hideText.setText(getString(R.string.hide));
						checkWord.setText(valuesBD.get(wordId-1).getEnglish());
					}
				}
			});
		}
		else {
			speech.setText(R.string.speech);
			speech.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
			          	promptSpeechInput();
			        }
				});
		}
		
		datasource = new WordsDataSource(this);
	    datasource.open();
	    
	    adError = new AlertDialog.Builder(CheckWords.this);
	    adError.setTitle("Ошибка");
        adError.setNeutralButton("Ок", new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            	dialog.cancel();

            	valuesBD = datasource.getAllWords();

            	answerWord.setText("");
            	setWordForCheck(language, checkWord, hideText);
            	if(hideText.getText().toString().equals(getString(R.string.show_word))) {
		        	tts.initQueue(valuesBD.get(wordId-1).getEnglish());
	        	}
            }
        });
        
        adConfirm = new AlertDialog.Builder(CheckWords.this);
        adConfirm.setTitle("Подтверждение");
        adConfirm.setPositiveButton("Да", new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            	dialog.cancel();
        		String answerWd = resultSpeech;
        		getAnswer(answerWd, language, answerWord, checkWord, hideText);
            }
        });
        adConfirm.setNeutralButton("Повторить", new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            	dialog.cancel();
            	promptSpeechInput();
            }
        });
        adConfirm.setNegativeButton("Поправить", new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            	dialog.cancel();
            	String answerWd = resultSpeech;
            	answerWord.setText(answerWd);
            }
        });
        
        valuesBD = datasource.getAllWords();
                
		if (valuesBD.size() == 0) {
			checkWord.setText("В базе нет слов!");
			answer.setEnabled(false);
			dontKnow.setEnabled(false);
			speech.setEnabled(false);
		}
		else {
			setWordForCheck(language, checkWord, hideText);
		}
		
		answer.setOnClickListener(
				new View.OnClickListener() {
		            public void onClick(View view) {
		            	if(!answerWord.getText().toString().isEmpty()) {
		            		String answerWd = answerWord.getText().toString();
		            		getAnswer(answerWd, language, answerWord, checkWord, hideText);
		            	}
		            	else {
		            		showToast("Не введен ответ!");
		            	}
		            }

	        });
		
		answer.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Word currentWd = (Word)valuesBD.get(wordId-1);
				long levelWd = currentWd.getLevel();
				datasource.changeLevel(currentWd, levelWd+2);
				
				if(language.equals("eng")) {
					showToast(currentWd.getRussian());
				}
				else if(language.equals("rus")) {
					showToast(currentWd.getEnglish());
				}
				
	        	valuesBD = datasource.getAllWords();
	        	answerWord.setText("");
	        	setWordForCheck(language, checkWord, hideText);
	        	if(hideText.getText().toString().equals(getString(R.string.show_word))) {
		        	tts.initQueue(valuesBD.get(wordId-1).getEnglish());
	        	}
				return true;
			}
		});
		
		dontKnow.setOnClickListener(
				new View.OnClickListener() {
		            public void onClick(View view) {
		            	Word currentWd = (Word)valuesBD.get(wordId-1);
		            	
		            	Log.d("myTag", String.valueOf(currentWd.getLevel()));
		            	
		            	//long levelWd = currentWd.getLevel();
		            	String checkWd = null;
		            	String resultWd = null;
		            	if(language.equals("eng")) {
		            		checkWd = currentWd.getEnglish();
		            		resultWd = currentWd.getRussian();
		            	}
		            	else if(language.equals("rus")) {
		            		checkWd = currentWd.getRussian();
		            		resultWd = currentWd.getEnglish();
		            	}
		            	
		            	adError.setMessage("\'" + checkWd + "\' - \'" + resultWd + "\'");
	            		adError.show();
	            		
	            		datasource.changeKnowledge(currentWd, 0);
	            		if(datasource.countZeroKnowledge() >= 10) {
        					Log.d("myTag", "Reset to repeat");
	            			resetToRepeat();
	        	            Thread threadChLev = new Thread() {
	        	        		public void run() {
	        	        			for(int i=0; i<valuesBD.size(); ++i) {
	        	        				if(valuesBD.get(i).getLevel() != 0)
	        	        					datasource.changeLevel((Word)valuesBD.get(i), 3);
	        	        			}
	        	        		}
	        	        	};
	        	        	threadChLev.start();
	            		}
		            }

					private void resetToRepeat() {
						List<Word> zeroKnowledge = datasource.getZeroKnowledge();
						int size = zeroKnowledge.size();
						for(int i=0; i<size; ++i) {
							datasource.changeLevel((Word)zeroKnowledge.get(i), 0);
							datasource.changeKnowledge((Word)zeroKnowledge.get(i), 1);
						}
					}
				});
	}
	
	private void getAnswer(String answerWd, String language, EditText answerWord, TextView checkWord, Button hideText) {
		Word currentWd = (Word)valuesBD.get(wordId-1);
    	long levelWd = currentWd.getLevel();
    	
    	Log.d("myTag", String.valueOf(levelWd));
    	
    	String checkWd = null;
    	String resultWd = null;
    	if(language.equals("eng")) {
    		checkWd = currentWd.getEnglish();
    		resultWd = currentWd.getRussian();
    	}
    	else if(language.equals("rus")) {
    		checkWd = currentWd.getRussian();
    		resultWd = currentWd.getEnglish();
    	}

    	if (!answerWd.equals(resultWd)) {
    		adError.setMessage("\'" + checkWd + "\' - \'" + resultWd + "\'");
    		adError.show();
    	}
    	else {
    		datasource.changeLevel(currentWd, levelWd+1);
    		if (levelWd+1 > 10) {
    			Thread threadChLev = new Thread() {
    				public void run() {
    					for(int i=0; i<valuesBD.size(); ++i) {
    						datasource.changeLevel((Word)valuesBD.get(i), 1);
    					}
    				}
    			};
    			threadChLev.start();
    		}
    		showToast("Правильно!");

        	valuesBD = datasource.getAllWords();

        	answerWord.setText("");
        	setWordForCheck(language, checkWord, hideText);
        	if(hideText.getText().toString().equals(getString(R.string.show_word))) {
	        	tts.initQueue(valuesBD.get(wordId-1).getEnglish());
        	}
    	}
	}
	
	private void setWordForCheck(String language, TextView checkWord, Button hideText) {
		wordId = getWordNumber(valuesBD);
		if(hideText.getText().toString().equals(getString(R.string.show_word))) {
			checkWord.setText("Воспринимайте на слух!");
		}
		else if(language.equals("eng")) {
			checkWord.setText(valuesBD.get(wordId-1).getEnglish());
		}
		else if(language.equals("rus")){
			checkWord.setText(valuesBD.get(wordId-1).getRussian());
		}
		else {
			checkWord.setText("Ошибка поиска слова!");
		}
	}

	private int getWordNumber(List<Word> valuesBD) {
		
		long minLevel = datasource.getMinLevel();
		int sizeBD = valuesBD.size();

		int countMin = 0;
		for(int i=0; i<sizeBD; ++i) {
			if(minLevel == valuesBD.get(i).getLevel()) {
				countMin++;
			}
		}
		
		int[] idWordsWithMinLevel = new int[countMin];
		int j = 0;
		for(int i=0; i<sizeBD; ++i) {
			if(minLevel == valuesBD.get(i).getLevel()) {
				idWordsWithMinLevel[j++] = i+1;
			}
		}
		Random rand = new Random();
		int wordId = rand.nextInt(j);
		
		return idWordsWithMinLevel[wordId];
	}

	private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultSpeech = result.get(0);
                adConfirm.setMessage("\'" + resultSpeech + "\'");
                adConfirm.show();
            }
            break;
        }
 
        }
    }
	
	@Override
	public void onBackPressed()
	{
		datasource.close();
		finish();
	}

}
