package com.churchkit.churchkit.api;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.Pexel.PhotoPexelsDao;
import com.churchkit.churchkit.database.entity.PexelsPhoto;
import com.churchkit.churchkit.repository.song.BaseRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PexelsRepository extends BaseRepository<PhotoPexelsDao,PexelsPhoto> {
    private final String baseEndpointsImg ="https://api.pexels.com/v1/";
    private final String baseEndpointsVideo ="https://api.pexels.com/videos/";
    private final String API_KEY = "hDvX7tPfLKco3c9nDpRyZBrWxzmxcz9NHLbcUhTqoLBMlVy1efQ44B0s";
    private final String requestCollections = "https://api.pexels.com/v1/collections?per_page=1"; //get all collection

    private final String request = "https://api.pexels.com/v1/collections/";



    public PexelsRepository(PhotoPexelsDao dao) {
        super(dao);
    }

    public void  insert(PexelsPhoto pexelsPhoto){
        dao.insert(pexelsPhoto);
    }
    /*public void  insertAll(List<PexelsPhoto> pexelsPhoto){
        dao.insertAll(pexelsPhoto);
    }*/

    @Override
    public void insertAll(List<PexelsPhoto> entity) {
        super.insertAll(entity);
    }

    public void  delete(PexelsPhoto pexelsPhoto){
        dao.delete(pexelsPhoto);
    }

    public LiveData<List<PexelsPhoto>> getAllPexelsPhoto(){
       return dao.getAllPhotoPexels();
    }



    public Flowable<  List<PexelsPhoto> > makeJ(String id){
        if (id==null)
            id = "btoaaaq";

        final String finalId = id;
        return Flowable.fromCallable( ()->{

            URL url = new URL( request+ finalId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", API_KEY);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println("afiche repo: "+response.toString());
                reader.close();
                return jsonToPexels( response.toString() );
            } else {
                throw new IOException("Request failed with response code: " + responseCode);
            }
        } ).subscribeOn(Schedulers.io());
    }

    public  List<PexelsPhoto> jsonToPexels(String photoJSon) throws JSONException {
        List<PexelsPhoto>photoList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(photoJSon);
        JSONArray jsonArray = jsonObject.getJSONArray("media");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPhoto = jsonArray.getJSONObject(i);
            String type = jsonPhoto.getString("type");

            if (type.equalsIgnoreCase("Photo")){
                long id = jsonPhoto.getLong("id");
                int width = jsonPhoto.getInt("width");
                int height = jsonPhoto.getInt("height");
                String url = jsonPhoto.getString("url");
                String avgColor = jsonPhoto.getString("avg_color");
                String photographer = jsonPhoto.getString("photographer");
                String alt = jsonPhoto.getString("alt");

                JSONObject urlImgObjects = jsonPhoto.getJSONObject("src");
                String urlMedium = urlImgObjects.getString("medium");
                String urlOriginal = urlImgObjects.getString("original");
                String urlTiny = urlImgObjects.getString("tiny");
                String urlPortrait = urlImgObjects.getString("portrait");
                String urlLandscape = urlImgObjects.getString("landscape");
                String urlSmall = urlImgObjects.getString("small");

                photoList.add(
                        new PexelsPhoto(
                                id,width,height,url,photographer,
                                avgColor,urlOriginal,urlMedium,urlSmall,urlTiny,
                                urlPortrait,urlLandscape,alt
                        )
                );

            }


        }
        return photoList;
    }


}
