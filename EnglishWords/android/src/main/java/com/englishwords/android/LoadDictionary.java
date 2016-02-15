package com.englishwords.android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.englishwords.R;
import com.englishwords.common.WordsLoadDictionary;

public class LoadDictionary extends MainActivity {

    private Spinner firstLanguage;
    private Spinner secondLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_dictionary);

        firstLanguage = (Spinner)findViewById(R.id.spinner1);
        secondLanguage = (Spinner)findViewById(R.id.spinner2);
        Button loadDictionary = (Button)findViewById(R.id.button);

        WordsLoadDictionary myWorldLoadDictionary = new WordsLoadDictionary();
        String[] languages = myWorldLoadDictionary.getLanguageList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        firstLanguage.setAdapter(adapter);
        secondLanguage.setAdapter(adapter);

        firstLanguage.setSelection(0);
        secondLanguage.setSelection(1);

        loadDictionary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String first = firstLanguage.getSelectedItem().toString();
                String second = secondLanguage.getSelectedItem().toString();
                if (first.equals(second)) {
                    showToast("Выберите разные языки!");
                }
                else {
                    // loadDictionary(first, second);
                }
                break;
        }
    }
}
