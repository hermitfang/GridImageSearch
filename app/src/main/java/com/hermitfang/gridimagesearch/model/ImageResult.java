package com.hermitfang.gridimagesearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageResult implements Serializable {

    private static final long serialVersionUID = -2893089570992474768L; //how to auto generate?
    public String fullUrl;
    public String thumbUrl;
    public String title;
    public int width;
    public int height;
    public int thumbWidth;
    public int thumbHeight;

    // json: passing the raw JSONArray from API
    public ImageResult (JSONObject json) {
        try {
            fullUrl = json.getString("url");
            thumbUrl = json.getString("tbUrl");
            title = json.getString("title");
            width = json.getInt("width");
            height = json.getInt("height");
            thumbWidth = json.getInt("tbWidth");
            thumbHeight = json.getInt("tbHeight");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //
    public static ArrayList<ImageResult> fromJSONArray (JSONArray arr) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i=0; i<arr.length(); i++) {
            try {
                results.add(new ImageResult(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
