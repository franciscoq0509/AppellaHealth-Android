package com.app.appellahealth.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Set;

public class Prefs {
	private SharedPreferences pref;
	private Editor editor;
	private int PRIVATE_MODE = 0;
	private static final String PREF_NAME = Prefs.class.getPackage().getName();

	// Keys
	public static final String KEY_user = "user";
	public static final String KEY_is_logged_in = "is_logged_in";
	public static final String KEY_content_pane_height = "content_pane_height";

	// Values
	public static final String DEF_null = null;
	public static final Boolean DEF_true = true;
	public static final Boolean DEF_false = false;
	public static final String DEF_empty = "";
	public static final int DEF_zero = 0;
	public static final int DEF_minus_one = -1;

	// Constructor
	public Prefs() {
	}

	public Prefs(Context mContext) {
		getPrefs(mContext);
	}


	public void getPrefs(Context mContext) {
		pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	}

	public Prefs editPrefs() {
		editor = pref.edit();
		return this;
	}

	public void commitPrefs() {
		editor.commit();
	}

	public Prefs putBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		return this;
	}

	public boolean getBoolean(String key, boolean default_value) {
		return pref.getBoolean(key, default_value);
	}

	public Prefs putString(String key, String value) {
		editor.putString(key, value);
		return this;
	}

	public String getString(String key, String default_value) {
		return pref.getString(key, default_value);
	}

	public Prefs putInt(String key, int value) {
		editor.putInt(key, value);
		return this;
	}

	public int getInt(String key, int default_value) {
		return pref.getInt(key, default_value);
	}

	public Prefs putLong(String key, long value) {
		editor.putLong(key, value);
		return this;
	}

	public long getLong(String key, long default_value) {
		return pref.getLong(key, default_value);
	}
	
	public Prefs putFloat(String key, float value) {
		editor.putFloat(key, value);
		return this;
	}

	public float getFloat(String key, float default_value) {
		return pref.getFloat(key, default_value);
	}

	public Prefs putStringSet(String key, Set<String> value) {
		editor.putStringSet(key, value);
		return this;
	}

	public Set<String> getStringSet(String key, Set<String> default_value) {
		return pref.getStringSet(key, default_value);
	}

	public Prefs putObject(String key, Object object) {
		if (object == null) {
			throw new IllegalArgumentException("Object is null");
		}
		if (key.equals("") || key == null) {
			throw new IllegalArgumentException("Key is empty or null");
		}
		editor.putString(key, new Gson().toJson(object));
		return this;
	}

	public Object getObject(String key, Type type) {
		String jsonString = pref.getString(key, null);
		if (jsonString == null) {
			return null;
		}
		else {
			try {
				return new Gson().fromJson(jsonString, type);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Object stored with key "
						+ key + " is instance of other class");
			}
		}
	}
	public Prefs removeKey(String key)
	{
		editor.remove(key);
		return this;
	}
	
	public Prefs clearAllPrefs()
	{
		editor.clear();
		return this;
	}
}
