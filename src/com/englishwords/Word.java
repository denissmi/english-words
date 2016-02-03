package com.englishwords;

public class Word {
	private long id;
	private String english;
	private String russian;
	private long level;
	private long knowledge;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}
	
	public String getRussian() {
		return russian;
	}

	public void setRussian(String russian) {
		this.russian = russian;
	}
	
	public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}
	
	public long getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(long knowledge) {
		this.knowledge = knowledge;
	}
}
