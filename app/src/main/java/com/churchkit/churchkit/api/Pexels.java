package com.churchkit.churchkit.api;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.churchkit.churchkit.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Pexels {
    private final String baseEndpointsImg ="https://api.pexels.com/v1/";
    private final String baseEndpointsVideo ="https://api.pexels.com/videos/";
    private final String API_KEY = "hDvX7tPfLKco3c9nDpRyZBrWxzmxcz9NHLbcUhTqoLBMlVy1efQ44B0s";
    private final String requestCollections = "https://api.pexels.com/v1/collections?per_page=1"; //get all collection


    String imageUrl = "YOUR_IMAGE_URL";

   /* GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
            .addHeader("Authorization", apiKey)
            .build());

        Glide.with(activity)
            .load(glideUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.abbr_color) // Optional placeholder image
                        .error(R.drawable.about_24)) // Optional error image
            .into(new ImageView(activity));*/
    public Pexels() throws IOException {
        URL url = new URL( requestCollections );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", API_KEY);


        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();


            Gson gson = new Gson();



            System.out.println("Total results: " + response.toString());


        } else {

            System.out.println("API request failed with response code: " + responseCode);
        }
    }


}
