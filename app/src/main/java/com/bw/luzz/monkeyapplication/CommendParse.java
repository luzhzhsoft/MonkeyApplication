package com.bw.luzz.monkeyapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Luzz on 2016/3/9.
 */
public class CommendParse {
    public static ArrayList<EventCommand> jsonToEventCommands(String json){
        ArrayList<EventCommand> commands=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                EventCommand eventCommand=new EventCommand(jsonObject.getString("mClassName"),jsonObject.getString("mEventType"),jsonObject.getString("mNodeType"),jsonObject.getString("mAction"),jsonObject.getString("mTextValue"));
                commands.add(eventCommand);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commands;
    }

}
