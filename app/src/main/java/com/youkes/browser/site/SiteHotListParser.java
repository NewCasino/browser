package com.youkes.browser.site;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SiteHotListParser {
	
	public static ArrayList<SiteHotListItem> parse(String jsonstr)
	{
		ArrayList<SiteHotListItem> list=new ArrayList<SiteHotListItem>();
	    try{
	        JSONObject jsonResult=new JSONObject(jsonstr);
			if(jsonResult.has("api")){
				if("/error/network".equals(jsonResult.getString("api"))){
					return null;
				}
			}

	        if(!jsonResult.has("content")){
        		return null;
        	}
	        
	        JSONArray docs=jsonResult.getJSONArray("content");
	        
	        int lenDocs=docs.length();
	        for(int i=0;i<lenDocs;i++){
	        	JSONObject docObj=docs.getJSONObject(i);
	        	SiteHotListItem info=new SiteHotListItem(docObj);
	        	list.add(info);
	        	
	        }
	        
	     
	    }catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}


	
	
}
