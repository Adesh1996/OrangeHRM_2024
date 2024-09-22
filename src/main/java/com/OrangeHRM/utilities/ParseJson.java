package com.OrangeHRM.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParseJson {

	private ParseJson() {
		throw new IllegalStateException("Utility Class");
	}

	public static Map<String, String> jsonToMap(JSONObject json) {
		Map<String, String> retMap = new HashMap<>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, String> toMap(JSONObject object) {
		Map<String, String> map = new HashMap<>();
		String val = null;
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			val = value.toString();
			map.put(key, val);

		}
		return map;
	}

	public static List<Object> toList(JSONArray array) {
		List<Object> list = new ArrayList<>();

		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	@SuppressWarnings("serial")
	public static Map<String, String> jsonToMap(String jsonString) {
		Map<String, String> map = null;
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		map = gson.fromJson(jsonString, type);
		return map;
	}
	
	@SuppressWarnings("serial")
	public static Map<String, Object> jsonToMap_new(String jsonString) {
		Map<String, Object> map = null;
		Gson gson = new GsonBuilder().setLenient().create();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		map = gson.fromJson(jsonString, type);
		return map;
	}
	
	
	public static List<Map<String, String>> getJsonArrayToListOfMap(String jsonString, String data){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONArray jsonArray = jsonObj.getJSONArray(data);
		
		for(Object array: jsonArray) {
			list.add(jsonToMap(array.toString()));
		}
		return list;
	}
	
	//Used to convert response with multiple values under one key
	public static Map <String, String> jsonToMap_old(String jsonString){
		Map<String, String> map = new HashMap<>();
		try {
			InputStreamReader inputstream = new InputStreamReader(new 
					ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8)));
			BufferedReader br = new BufferedReader(inputstream);
			
			String line = br.readLine();
			while(!line.contains("value"))
				line= br.readLine();
			line = br.readLine();
			String[] splittedLine = null;
			while (line != null) {
				if(line.indexOf(':') != -1) {
					splittedLine = line.split(":");
					splittedLine[0] = splittedLine[0].replaceAll("\"", "").trim();
					splittedLine[1] = splittedLine[1].replaceAll("\"", "");
					splittedLine[1] = splittedLine[1].replaceAll(",", "").trim();
					splittedLine[1] = splittedLine[1].replaceAll(".0", "").trim();
					map.put(splittedLine[0], splittedLine[1]);
				}
				line = br.readLine();
			}
			br.close();
		}
		catch (IOException e) {
			Assert.fail(" ", e.getCause());
		}
		return map;
	}
}
