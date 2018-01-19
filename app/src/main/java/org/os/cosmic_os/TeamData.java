package org.os.cosmic_os;


import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

class TeamData {
    String developerName, description, github, devImage, bgColor, thread, telegram;

    static ArrayList<TeamData> getDevelopersFromFile(Context context){
        final ArrayList<TeamData> devList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset(context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonArray = json.getJSONArray("dev");

            // Get Team objects from data
            for(int i = 0; i < jsonArray.length(); i++){
                TeamData dev = new TeamData();

                dev.developerName = jsonArray.getJSONObject(i).getString("developerName");
                dev.description = jsonArray.getJSONObject(i).getString("description");
                dev.github = jsonArray.getJSONObject(i).getString("github");
                dev.devImage = jsonArray.getJSONObject(i).getString("image");
                dev.bgColor = jsonArray.getJSONObject(i).getString("bg_color");
                dev.thread = jsonArray.getJSONObject(i).getString("thread");
                dev.telegram = jsonArray.getJSONObject(i).getString("telegram");
                devList.add(dev);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return devList;
    }

    private static String loadJsonFromAsset(Context context) {
        String json = null;

        try {
            InputStream inputStream;
            String filePath = context.getExternalFilesDir(null) + "/" + "team.json";
            File file = new File(filePath);
            if (file.exists()) inputStream = new FileInputStream(file);
            else inputStream = context.getAssets().open("team.json");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                int read = inputStream.read(buffer);
                if (read >= 0) {
                    inputStream.close();
                    json = new String(buffer, "UTF-8");
                }
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
