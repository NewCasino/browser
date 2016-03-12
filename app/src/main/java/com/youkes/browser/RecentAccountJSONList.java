package com.youkes.browser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xuming on 2016/3/7.
 */
public class RecentAccountJSONList {

    public static ArrayList<RecentAccountJSON> parse(String jsonstr) {
        ArrayList<RecentAccountJSON> list = new ArrayList<>();
        try {
            JSONArray jsonResult = new JSONArray(jsonstr);
            int lenDocs=jsonResult.length();
            for(int i=0;i<lenDocs;i++){
                JSONObject docObj=jsonResult.getJSONObject(i);
                RecentAccountJSON info=new RecentAccountJSON(docObj);
                list.add(info);
            }

        } catch (Exception e) {

        }
        return list;
    }

    public static String convertJSONString(ArrayList<RecentAccountJSON> list) {

        String result="[";
        for(int i=0;i<list.size();i++) {
            RecentAccountJSON json = list.get(i);
            result += "{";
            result += "'userId':'" + json.getUserId() + "',";
            result += "'accessKey':'" + json.getAccessKey() + "',";
            result += "'name':'" + json.getName() + "',";
            result += "'photo':'" + json.getPhoto() + "'";
            result += "}";
            if(i<list.size()-1){
                result += ",";
            }

        }
        result+="]";
        return result;
    }

    public static boolean isContainUserId(ArrayList<RecentAccountJSON> list,String userId) {
        for(int i=0;i<list.size();i++){
            RecentAccountJSON json=list.get(i);
            if(json.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    public static boolean removeByUserId(ArrayList<RecentAccountJSON> list,String userId) {
        for(int i=0;i<list.size();i++){
            RecentAccountJSON json=list.get(i);
            if(json.getUserId().equals(userId)){
                list.remove(i);
                return true;
            }
        }
        return false;
    }

    public static void addItem(ArrayList<RecentAccountJSON> list, RecentAccountJSON item) {
        if(item==null){
            return;
        }
        if(isContainUserId(list,item.getUserId())){
            removeByUserId(list,item.getUserId());
        }
        list.add(0,item);
    }
}

