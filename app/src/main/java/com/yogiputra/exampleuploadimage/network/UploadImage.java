package com.yogiputra.exampleuploadimage.network;


import com.squareup.okhttp.RequestBody;


import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by koba on 12/11/15.
 */
public interface UploadImage {
    @Multipart()
    @POST("save.php")
    Call<UploadResult> upload(@Part("attachment\"; filename=\"image.jpg\" ") RequestBody file,
                              @Part("description") String description,
                              @Part("id") String id
    );



}
