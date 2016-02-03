package com.englishwords;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WordAdapter extends BaseAdapter {

	Context mContext;
	List<Word> wds;

	public WordAdapter(Context context, List<Word> values) {
		mContext = context;
		wds = values;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Wd wd;
		LayoutInflater l_Inflater = LayoutInflater.from(mContext);
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.words_row, null);
			wd = new Wd();
			wd.engAndRus = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(wd);
		}
		else {
			wd = (Wd) convertView.getTag();
		}

		wd.engAndRus.setText(getItemEnglish(position) + " - " + getItemRussian(position));

		return convertView;
	}

	@Override
	public int getCount() {
		return wds.size();
	}

	@Override
	public Object getItem(int position) {
		return wds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return wds.get(position).getId();
	}

	public String getItemEnglish(int position) {
		return wds.get(position).getEnglish();
	}

	public String getItemRussian(int position) {
		return wds.get(position).getRussian();
	}

	static class Wd {
		TextView engAndRus;
	}
}
