package com.ase.aplicatiemuzicamobila;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    MediaPlayer music;
    ImageView imgV;

    private ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            music = MediaPlayer.create(getApplicationContext(),o);

            MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(getApplicationContext(), o);

            try{
                byte[] image=mediaMetadataRetriever.getEmbeddedPicture();
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(image)
                        .placeholder(R.drawable.missing)
                        .into(imgV);

            }catch (Exception e){
                Log.e("Exception audio file image",e.getMessage());
            }finally {
                try {
                    mediaMetadataRetriever.release();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgV=findViewById(R.id.imageView);
        TextView load=findViewById(R.id.btnLoad);
        load.setOnClickListener(v -> launcher.launch("audio/*"));

        Map<String, String> songList=new HashMap<>();
        ArrayList<String> audioLists = new ArrayList<>();

        String[] strings = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME};

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, strings, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int audioIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    songList.put(cursor.getString(audioIndex),cursor.getString(audioIndex));
                    audioLists.add(cursor.getString(audioIndex));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        for (int i = 0 ; i < audioLists.size(); i++ ){
            Log.e("audioListss" ,"Audio Path ---->>>  " + audioLists.get(i));
        }

        music = MediaPlayer.create(this,R.raw.bna);

    }

    public void playSong(View v){
        music.start();
    }

    public void pauseSong(View v) {
        music.pause(); }

    public void stopSong(View v) {
        music.stop();
        music = MediaPlayer.create(this, R.raw.bna);
        Glide.with(getApplicationContext())
                .load(R.drawable.missing)
                .into(imgV);
    }
}

