package com.ase.aplicatiemuzicamobila.FunctionalClasses;


import android.app.Activity;
import android.media.MediaPlayer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.ase.aplicatiemuzicamobila.R;
import com.bumptech.glide.Glide;

public class SongPlayer {
    private static SongPlayer btnInstance=null;
    private ImageButton btnPlayPause;
    private ImageButton btnStop;
    private ImageView imgVPlaying;
    private ImageView imgV;
    private MediaPlayer mediaPlayer;
    private Activity activity;

    TextView tvSongName;
    TextView tvCurrentTime;
    TextView tvFinalTime;
    SeekBar seekBar;

    private SongPlayer() {
    }

    private SongPlayer(ImageButton btn, ImageView imgVVynil, MediaPlayer media, Activity activity){
        this.btnPlayPause=btn;
        this.imgVPlaying=imgVVynil;
        this.activity=activity;
        this.tvSongName=activity.findViewById(R.id.tvSongName);
        this.mediaPlayer=media;

        this.mediaPlayer.setOnCompletionListener(mp -> {
            pauseSong();
        });

        imgV=activity.findViewById(R.id.imageView);
        tvCurrentTime=activity.findViewById(R.id.tvDurationCurrent);

        tvFinalTime=activity.findViewById(R.id.tvDurationFinal);
        tvFinalTime.setText(milliSecondsToTimer((long) this.mediaPlayer.getDuration()));

        seekBar=activity.findViewById(R.id.seekBar);
        seekBar.setMax(this.mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                    updatePlayer(mediaPlayer.getCurrentPosition());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        this.btnStop=activity.findViewById(R.id.btnStop);
//        this.btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopSong();
//            }
//        });

        this.btnPlayPause.setOnClickListener(v -> {
            Animation animation= AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.vynil_animation);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setDuration(3000);

            if(mediaPlayer.isPlaying()){
                pauseSong();
            }else{
                playSong(animation);
            }
        });
    }

    public static SongPlayer getPlayPauseInstance(ImageButton btn, ImageView imgVVynil, MediaPlayer media, Activity activity) {
        if(btnInstance==null){
            btnInstance=new SongPlayer(btn,imgVVynil,media,activity);
        }
        return btnInstance;
    }

    public static void releaseMediaPlayer(){
        btnInstance.mediaPlayer.release();
    }

    public void playSong(Animation animation){
        btnInstance.mediaPlayer.start();
        this.imgVPlaying.startAnimation(animation);
        seekBar.post(mUpdateSeek);
        btnPlayPause.setBackground(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.pause,null));
    }

    public void pauseSong() {
        btnInstance.mediaPlayer.pause();
        this.imgVPlaying.clearAnimation();
        btnPlayPause.setBackground(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.play,null));
    }

    public void stopSong() {
        pauseSong();
        btnInstance.mediaPlayer.stop();
        tvSongName.setText("Load another song");
        Glide.with(activity.getApplicationContext())
                .load(R.drawable.missing)
                .into(imgV);
        updateTotalDuration();
    }

    public void changeMediaPlayer(MediaPlayer media){
        btnInstance.mediaPlayer.release();
        this.mediaPlayer=media;
        this.mediaPlayer.setOnCompletionListener(mp -> {
            pauseSong();
        });
    }

    public MediaPlayer getMediaPlayer() {
        return btnInstance.mediaPlayer;
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

    private final Runnable mUpdateSeek = new Runnable() {
        public void run() {
            int currentDuration;
            if (btnInstance.mediaPlayer.isPlaying()) {
                currentDuration = btnInstance.mediaPlayer.getCurrentPosition();
                updatePlayer(currentDuration);
                seekBar.setProgress(currentDuration);
                seekBar.postDelayed(this,0);
            }else {
                seekBar.removeCallbacks(this);
            }
        }
    };

    private void updatePlayer(int currentDuration){
        tvCurrentTime.setText("" + milliSecondsToTimer((long) currentDuration));
    }

    public void updateTotalDuration(){
        tvFinalTime.setText(milliSecondsToTimer((long) this.mediaPlayer.getDuration()));
        seekBar.setProgress(0);
        updatePlayer(0);
        seekBar.setMax(this.mediaPlayer.getDuration());
    }
}
