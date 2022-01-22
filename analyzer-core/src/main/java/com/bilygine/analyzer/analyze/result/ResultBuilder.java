package com.bilygine.analyzer.analyze.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to build a result trough all steps.
 * @author esteban
 */

public class ResultBuilder {

	public static final String TEXT = "text";
	public static final String TIMELINE = "timeline";
	public static final String TRANSCRIPT = "transcript";
	public static final String WORDS = "words";
	public static final String WORDS_COUNT = "wordsCount";
	public static final String VOLUME = "volume";

	private Map<String, Object> root;

	public ResultBuilder() {
		root = new LinkedHashMap<>(1);
	}

	public ResultBuilder(Map<String, Object> root) {
		this.root = root;
	}

	/**
	 * Register build
	 * Helper for result formatting
	 * @param name
	 * @return
	 */
	public ResultBuilder get(String name) {
		Object tmp = root.get(name);
		if (tmp == null) {
			tmp = new LinkedHashMap<String, Object>(1);
			root.put(name, tmp);
		}
		return new ResultBuilder((Map<String, Object>)tmp);
	}

	/**
	 * Register a key/value into timeline
	 *
	 * @param second
	 * @param key
	 * @param value
	 */
	public void registerToTimeline(int second, String key, Object value) {
		get(TIMELINE).get(String.valueOf(second)).put(key, value);
	}

	/**
	 * Allow us to init word
	 *
	 * @param second
	 * @param word
	 * @param start
	 * @param end
	 */
	public void registerWord(int second, String word, long start, long end) {
		ResultBuilder rb = get(TIMELINE).get(String.valueOf(second));
		if (!rb.exists(WORDS)) {
			rb.put(WORDS, new ArrayList<>());
		}
		//
		Map<String, Object> map = new HashMap<>();
		map.put("word", word);
		map.put("start", start);
		map.put("end", end);
		//
		((ArrayList) rb.getRoot().get(WORDS)).add(map);
		// Increment words count
		int count = 0;
		if (getRoot().containsKey(WORDS_COUNT)) {
			count = Integer.parseInt(getRoot().get(WORDS_COUNT).toString());
		}
		getRoot().put(WORDS_COUNT, ++count);
		// Increment full text
		String text = "";
		if (getRoot().containsKey(TEXT)) {
			text = getRoot().get(TEXT).toString();
		}
		if (!text.isEmpty()) {
			text += " ";
		}
		text += word;
		getRoot().put(TEXT, text);
	}

	private boolean exists(String wordsKey) {
		return getRoot().containsKey(wordsKey);
	}

	public void put(String name, Object value) {
		root.put(name, value);
	}

	public Map<String, Object> getRoot() {
		return this.root;
	}

}
