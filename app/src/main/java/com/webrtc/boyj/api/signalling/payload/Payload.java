package com.webrtc.boyj.api.signalling.payload;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class Payload implements Serializable {

    @NonNull
    public String toJson() {
        final Gson gson = new Gson();

        return gson.toJson(this);
    }
    public JSONObject toJsonObject(){
        final Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
