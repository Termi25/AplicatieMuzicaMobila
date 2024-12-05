package com.ase.aplicatiemuzicamobila.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatiemuzicamobila.AudioListRecycleView.AdapterAudioList;
import com.ase.aplicatiemuzicamobila.AudioListRecycleView.RecyclerItemClickListener;
import com.ase.aplicatiemuzicamobila.R;
import com.ase.aplicatiemuzicamobila.FunctionalClasses.SongPlayer;
import com.ase.aplicatiemuzicamobila.StorageClasses.AudioListElement;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    SongPlayer btnPlayPause;
    TextView tvSongName;
    TextView load;
    ImageView imgV;
    ImageView imgVAnimated;
    RecyclerView rlvSongList;
    AdapterAudioList adapterSongList;
    List<AudioListElement> songList=new ArrayList<>();
    Uri queryUri=null;

    private final ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            btnPlayPause.pauseSong();
            btnPlayPause.changeMediaPlayer(MediaPlayer.create(getApplicationContext(),o));
            btnPlayPause.updateTotalDuration();

            MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(getApplicationContext(), o);

            try{
                byte[] image=mediaMetadataRetriever.getEmbeddedPicture();
                if(image!=null){
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(image)
                            .placeholder(R.drawable.missing)
                            .into(imgV);
                }else{
                    Glide.with(getApplicationContext())
                            .load(R.drawable.missing)
                            .into(imgV);
                }

            }catch (Exception e){
                Log.e("Exception audio file image",e.toString());
            }
            try {
                String songTitle = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                if(songTitle==null){
                    songTitle=getFileName(o);
                }
                int length=songTitle.length();
                if(length>25){
                    tvSongName.setTextSize(10.0F);
                }else{
                    if(length>20){
                        tvSongName.setTextSize(16.0F);
                    }else{
                        tvSongName.setTextSize(24.0F);
                    }
                }

                tvSongName.setText(songTitle);
            } catch (Exception e) {
                Log.e("Exception Releasing MediaMetadataRetriever",e.toString());
            }
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        this.rlvSongList=findViewById(R.id.rlvSongs);
        this.adapterSongList=new AdapterAudioList(new AdapterAudioList.AudioElementDiff());
        this.rlvSongList.setAdapter(adapterSongList);
        this.rlvSongList.setLayoutManager(new LinearLayoutManager(this));

        this.rlvSongList.addOnItemTouchListener(new RecyclerItemClickListener(this,this.rlvSongList ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AudioListElement item=adapterSongList.getCurrentList().get(position);
                btnPlayPause.pauseSong();
                btnPlayPause.changeMediaPlayer(MediaPlayer.create(getApplicationContext(),item.getFileUri()));
                btnPlayPause.updateTotalDuration();

                MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(getApplicationContext(), item.getFileUri());
                try{
                    byte[] image=mediaMetadataRetriever.getEmbeddedPicture();
                    if(image!=null){
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(image)
                                .placeholder(R.drawable.missing)
                                .into(imgV);
                    }else{
                        Glide.with(getApplicationContext())
                                .load(R.drawable.missing)
                                .into(imgV);
                    }

                }catch (Exception e){
                    Log.e("Exception audio file image",e.toString());
                }
                try {
                    String songTitle = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    if(songTitle==null){
                        songTitle=getFileName(item.getFileUri());
                    }
                    int length=songTitle.length();
                    if(length>25){
                        tvSongName.setTextSize(10.0F);
                    }else{
                        if(length>20){
                            tvSongName.setTextSize(16.0F);
                        }else{
                            tvSongName.setTextSize(24.0F);
                        }
                    }

                    tvSongName.setText(songTitle);
                } catch (Exception e) {
                    Log.e("Exception Releasing MediaMetadataRetriever",e.toString());
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 0);
        }

        if(Build.VERSION.SDK_INT>=29){
            this.queryUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else{
            this.queryUri = MediaStore.Files.getContentUri("external");
        }

        String[] filtering=new String[]{"audio/%"};
        try(Cursor cursor=this.getContentResolver().query(queryUri,
                new String[]{MediaStore.Files.FileColumns._ID,MediaStore.Files.FileColumns.DISPLAY_NAME},
                null,
                null,
                null)){

            int idColumn=cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            int nameColumn=cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);

            while(cursor.moveToNext()){
                long id=cursor.getLong(idColumn);
                String name=cursor.getString(nameColumn);
                int mediaType=0;

                if(name.contains(".mp3")){
                    mediaType=1;
                }

                if(name.contains(".flac")){
                    mediaType=2;
                }

                if(name!=null && mediaType!=0){
                    Uri contentUri= ContentUris.withAppendedId(queryUri,id);
                    System.out.println(name);
                    songList.add(new AudioListElement(contentUri,name,mediaType));
                }
            }
        }
        this.adapterSongList.submitList(songList);
        this.rlvSongList.getAdapter().notifyDataSetChanged();

        imgV = findViewById(R.id.imageView);
        imgVAnimated=findViewById(R.id.imgVVynil);
        imgVAnimated.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.music_app_logo4,null));

        tvSongName = findViewById(R.id.tvSongName);
        load = findViewById(R.id.btnLoad);
        load.setOnClickListener(v -> launcher.launch("audio/*"));

        btnPlayPause = SongPlayer.getPlayPauseInstance(findViewById(R.id.btnPlay),imgVAnimated,MediaPlayer.create(this,R.raw.bna),this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SongPlayer.releaseMediaPlayer();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst() ) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        result=result.split("\\.")[0];
        return result;
    }
}

