package com.ase.aplicatiemuzicamobila;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideExtension;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    PlayButton btnPlayPause;
    TextView tvSongName;
    TextView tvCurrentTime;
    TextView tvFinalTime;
    SeekBar seekBar;
    TextView load;
    ImageView imgV;
    ImageView imgVAnimated;

    private final ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            btnPlayPause.pauseSong(getApplicationContext());
            btnPlayPause.setPlaying(false);
            btnPlayPause.changeMediaPlayer(MediaPlayer.create(getApplicationContext(),o));

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
        setContentView(R.layout.activity_main);

        imgV = findViewById(R.id.imageView);
        imgVAnimated=findViewById(R.id.imgVVynil);
        imgVAnimated.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.vynil,null));

        tvSongName = findViewById(R.id.tvSongName);
        tvCurrentTime=findViewById(R.id.tvDurationCurrent);
        tvFinalTime=findViewById(R.id.tvDurationFinal);
        load = findViewById(R.id.btnLoad);
        load.setOnClickListener(v -> launcher.launch("audio/*"));

        btnPlayPause = PlayButton.getPlayPauseInstance(findViewById(R.id.btnPlay),imgVAnimated,MediaPlayer.create(this,R.raw.bna),getApplicationContext());
    }

    public void stopSong(View v) {
        btnPlayPause.stopSong();
        Glide.with(getApplicationContext())
                .load(R.drawable.missing)
                .into(imgV);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayButton.releaseMediaPlayer();
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

    public  String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private Runnable mUpdateTime = new Runnable() {
        public void run() {
            int currentDuration;
            if (btnPlayPause.isPlaying()) {
                currentDuration = btnPlayPause.getMediaPlayer().getCurrentPosition();
                updatePlayer(currentDuration);
                tvCurrentTime.postDelayed(this, 1000);
            }else {
                tvCurrentTime.removeCallbacks(this);
            }
        }
    };

    private void updatePlayer(int currentDuration){
        tvCurrentTime.setText("" + milliSecondsToTimer((long) currentDuration));
    }
}

