package com.example.dodolire;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {
    private static final String API_KEY =
            "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"; //
    private static final String CHAT_URL  = "https://api.openai.com/v1/chat/completions";
    private static final String IMAGE_URL = "https://api.openai.com/v1/images/generations";

    public static String generateStory(String prompt) {
        try {
            URL url = new URL(CHAT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("model", "gpt-3.5-turbo");
            JSONArray msgs = new JSONArray();
            msgs.put(new JSONObject()
                    .put("role", "system")
                    .put("content", "You are a helpful assistant."));
            msgs.put(new JSONObject()
                    .put("role", "user")
                    .put("content", prompt));
            body.put("messages", msgs);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes("utf-8"));
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8")
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

        } catch (Exception e) {
            Log.e("API", "Erreur generateStory", e);
            return null;
        }
    }

    public static String generateImage(String prompt) {
        try {
            URL url = new URL(IMAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("prompt", prompt);
            body.put("n", 1);
            body.put("size", "512x512");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes("utf-8"));
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8")
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            return json.getJSONArray("data")
                    .getJSONObject(0)
                    .getString("url");

        } catch (Exception e) {
            Log.e("API", "Erreur generateImage", e);
            return null;
        }
    }
}
