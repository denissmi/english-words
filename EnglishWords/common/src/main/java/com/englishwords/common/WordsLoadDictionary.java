package com.englishwords.common;

import java.io.UnsupportedEncodingException;

public class WordsLoadDictionary {

    private String[] data = {"Русский", "English", "Deutsch", "Français"};

    public String[] getLanguageList() {
        String[] myData = new String[data.length];
        for (int i=0; i<data.length; ++i) {
            try {
                byte bytes[] = data[i].getBytes("windows-1251");
                myData[i] = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return myData;
    }
}
