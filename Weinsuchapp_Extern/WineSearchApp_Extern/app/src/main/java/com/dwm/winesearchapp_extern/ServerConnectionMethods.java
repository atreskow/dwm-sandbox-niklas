package com.dwm.winesearchapp_extern;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnectionMethods {
    private static final Gson _gson = new Gson();


    public static JsonObject GetData(String service) {
        try {
            URL url = new URL(ServiceLocator.BASE_URL + service);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            String result;
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while (result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();

            JsonObject jsonObject = _gson.fromJson(result, JsonObject.class);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject PostData(String service, JSONObject data ) {
        try {
            URL url = new URL(ServiceLocator.BASE_URL + service);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(data.toString());
            osw.flush();
            osw.close();
            os.close();
            connection.connect();

            String result;
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while (result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();

            JsonObject jsonObject = _gson.fromJson(result, JsonObject.class);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
