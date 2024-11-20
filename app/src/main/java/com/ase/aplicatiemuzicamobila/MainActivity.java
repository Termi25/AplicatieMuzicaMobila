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

    PlayButton btnPlayPause;
    ImageView imgV;

    private final ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            btnPlayPause.changeMediaplayer(MediaPlayer.create(getApplicationContext(),o));

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
                Log.e("Exception audio file image",e.toString());
            }finally {
                try {
                    mediaMetadataRetriever.release();
                } catch (IOException e) {
                    Log.e("Exception Releasing MediaMetadataRetriever",e.toString());
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

        btnPlayPause=PlayButton.getPlayPauseInstance(findViewById(R.id.btnPlay),MediaPlayer.create(this,R.raw.bna),getApplicationContext());
    }

    public void stopSong(View v) {
        btnPlayPause.stopSong(v);
        Glide.with(getApplicationContext())
                .load(R.drawable.missing)
                .into(imgV);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayButton.releaseMediaPlayer();
    }
}

