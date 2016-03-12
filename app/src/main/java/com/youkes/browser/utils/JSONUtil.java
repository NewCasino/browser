package com.youkes.browser.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class JSONUtil {

	public static String[] getArrayString(JSONObject docObj, String name) {
		String[] list = null;
		if (docObj.has(name)) {
			JSONArray json;
			try {
				json = docObj.getJSONArray(name);
				int lenFeatures = json.length();
				list = new String[lenFeatures];
				for (int j = 0; j < lenFeatures; j++) {
					String f = json.getString(j);
					list[j] = f;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return list;

	}

	
	public static ArrayList<String> getArrayStrList(JSONObject docObj,
			String name) {
		ArrayList<String> list = new ArrayList<String>();
		if (docObj.has(name)) {
			JSONArray json;
			try {
				json = docObj.getJSONArray(name);
				int lenFeatures = json.length();

				for (int j = 0; j < lenFeatures; j++) {
					String f = json.getString(j);
					if(f!=null&&!f.equals("")){
						list.add(f);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return list;

	}

	public static double[] getArrayDouble(JSONObject docObj, String name) {
		double[] list = null;
		if (docObj.has(name)) {
			JSONArray json;
			try {
				json = docObj.getJSONArray(name);
				int lenFeatures = json.length();
				list = new double[lenFeatures];
				for (int j = 0; j < lenFeatures; j++) {
					double f = json.getDouble(j);
					list[j] = f;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return list;

	}

	public static int Type_STR = 1;
	public static int Type_Int = 2;
	public static int Type_Double = 3;

	public static int getInt(JSONObject docObj, String name) {
		int ret = 0;

		if (docObj.has(name)) {
			try {
				ret = docObj.getInt(name);
			} catch (JSONException e) {

			}
		}

		return ret;
	}


	public static long getLong(JSONObject docObj, String name) {
		long ret = 0;

		if (docObj.has(name)) {
			try {
				ret = docObj.getLong(name);
			} catch (JSONException e) {

			}
		}

		return ret;
	}

	public static double getDouble(JSONObject docObj, String name) {
		double ret = 0;

		if (docObj.has(name)) {
			try {
				ret = docObj.getDouble(name);
			} catch (JSONException e) {

			}
		}

		return ret;
	}

	public static String getString(JSONObject docObj, String name) {
		String ret = "";

		if (docObj.has(name)) {
			boolean isNull = docObj.isNull(name);
			if (isNull) {
				return "";
			}

			try {
				ret = docObj.getString(name);
				if (ret == null) {
					return "";
				}
			} catch (JSONException e) {
				return "";
			}

		}

		return ret;
	}

	public static boolean getBoolean(JSONObject docObj, String name) {
		boolean ret = false;

		if (!docObj.has(name)) {
			return false;
		}

		boolean isNull = docObj.isNull(name);
		if (isNull) {
			return false;
		}

		try {
			ret = docObj.getBoolean(name);
			return ret;
		} catch (JSONException e) {
			return false;
		}

	}
	
	public static JSONObject parseJSONObject(String result) {
		
		JSONObject jobj=null;
		try {
			jobj = new JSONObject(result);
		} catch (JSONException e) {

		}
		
		return jobj;
	}




	public static JSONObject getJSONObject(JSONObject docObj, String name) {
		JSONObject ret = null;

		if (docObj.has(name)) {
			try {
				ret = docObj.getJSONObject(name);
			} catch (JSONException e) {

			}
		}

		return ret;
	}

	public static JSONArray getJSONArray(JSONObject docObj, String name) {
		JSONArray ret = null;

		if (docObj.has(name)) {
			try {
				ret = docObj.getJSONArray(name);
			} catch (JSONException e) {

			}
		}

		return ret;
	}

	public static Date getLongDate(JSONObject docObj, String name) {

		if (docObj.has(name)) {
			try {
				long t = docObj.getLong(name);
				Date date = new Date(t);
				return date;
			} catch (JSONException e) {

			}
		}

		return null;
	}

	public static JSONObject getArrayJSONObject(JSONArray jarray, int i) {
		JSONObject ret = null;
		try {
			if (jarray == null) {
				return null;
			}
			if (i >= jarray.length()) {
				return null;
			}
			ret = (JSONObject) jarray.get(i);
			return ret;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ArrayList<String> getArrayStrList(String docStr){
		if(docStr==null){
			return null;
		}

		ArrayList<String> list=new ArrayList<String>();
		JSONArray json;
		try {
			json = new JSONArray(docStr);

			int l=json.length();
			for(int j=0;j<l;j++){
				String f=json.getString(j);
				list.add(f);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return list;
	}

	public static JSONObject getJSONArrayItem(JSONArray jarr, int i) {
		JSONObject jobj= null;
		try {
			jobj = (JSONObject)jarr.get(i);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj;
	}

	public static JSONArray toJSONArray(ArrayList<String> list) {
		JSONArray jarr=new JSONArray();
		if(list!=null) {
			for (String s : list) {
				jarr.put(s);
			}
		}
		return jarr;
	}
}
