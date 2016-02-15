package com.englishwords.android;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSManager {

    private TextToSpeech mTts = null;

    public TTSManager(Context context) {
        TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Thread threadTTS = new Thread() {
                        public void run() {
                            mTts.setLanguage(Locale.US);
                        }
                    };
                    threadTTS.start();
                }
            }
        };
        mTts = new TextToSpeech(context, onInitListener);
    }

    public void shutDown() {
        mTts.shutdown();
    }

    public void initQueue(String text) {
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
