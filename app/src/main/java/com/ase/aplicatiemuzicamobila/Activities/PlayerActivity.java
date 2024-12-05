package com.ase.aplicatiemuzicamobila.Activities;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.ase.aplicatiemuzicamobila.R;
import com.ase.aplicatiemuzicamobila.FunctionalClasses.SongPlayer;
import com.bumptech.glide.Glide;

public class PlayerActivity extends AppCompatActivity {

    SongPlayer btnPlayPause;
    TextView tvSongName;
    TextView load;
    ImageView imgV;
    ImageView imgVAnimated;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

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

