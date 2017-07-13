package com.qf.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qf.entity.HomeEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ken on 2017/3/27.
 */

public class JSONUtil {

    /**
     * 解析精选页的json
     * @param json
     * @return
     */
    public static List<HomeEntity> getHomeEntitys(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("sectionList");

            TypeToken<List<HomeEntity>> tt = new TypeToken<List<HomeEntity>>(){};
            return new Gson().fromJson(jsonArray.toString(), tt.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
