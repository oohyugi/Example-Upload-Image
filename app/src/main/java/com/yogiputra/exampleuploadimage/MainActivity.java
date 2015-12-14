package com.yogiputra.exampleuploadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yogiputra.exampleuploadimage.network.UploadImage;
import com.yogiputra.exampleuploadimage.network.UploadResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    ImageView gambar;
    private static final int CAMERA_PIC_REQUEST = 1111;
    TextView tv1;
    Button upload;
    String filename;
    File pictureFile;
    String nama;
    String url="http://192.168.1.8/uploadimage/";
    public static final String TAG = "LOG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        gambar=(ImageView)findViewById(R.id.gambar);
        tv1= (TextView)findViewById(R.id.tv1);
        upload=(Button)findViewById(R.id.btUpload);



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Retrofit uploadimage = new Retrofit.Builder().
                        baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(filename));
                UploadImage apiService = uploadimage.create(UploadImage.class);

                Call<UploadResult> reg = apiService.upload(file, "asu", "1");
                reg.enqueue(new Callback<UploadResult>() {

                    @Override
                    public void onResponse(Response<UploadResult> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Log error here since request failed
                    }
                });



            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        File pictureFileDir = getDir();
        if (requestCode == CAMERA_PIC_REQUEST) {
            //2


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            gambar.setImageBitmap(thumbnail);
            //3
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //4
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = "Picture_" + date + ".jpg";

            filename = pictureFileDir.getPath() + File.separator + photoFile;

            pictureFile = new File(filename);


            // File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");

            nama= pictureFile.getName();
            Toast.makeText(getApplicationContext(), String.valueOf(pictureFile), Toast.LENGTH_LONG).show();
            try {

                FileOutputStream fo = new FileOutputStream(pictureFile);
                //5
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
