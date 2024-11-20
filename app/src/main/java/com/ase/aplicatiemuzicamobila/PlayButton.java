package com.ase.aplicatiemuzicamobila;


import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

public class PlayButton {
    private boolean isPlaying;
    private static PlayButton btnInstance=null;
    private ImageButton btnPlayPause;
    private ImageView imgVPlaying;
    private MediaPlayer mediaPlayer;
    private Context context;

    private PlayButton() {
    }

    private PlayButton(ImageButton btn,ImageView imgVVynil, MediaPlayer media,Context ctx){
        this.btnPlayPause=btn;
        this.imgVPlaying=imgVVynil;
        this.isPlaying=false;
        this.mediaPlayer=media;
        this.context=ctx;

        this.btnPlayPause.setOnClickListener(v -> {
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.vynil_animation);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setDuration(1400);

            if(isPlaying){
                pauseSong(this.context);
                this.imgVPlaying.clearAnimation();
                this.imgVPlaying.setImageDrawable(ResourcesCompat.getDrawable(this.context.getResources(),R.drawable.vynil,null));
            }else{
                playSong(this.context);
                this.imgVPlaying.startAnimation(animation);
            }
            isPlaying=!isPlaying;
        });
    }

    public static PlayButton getPlayPauseInstance(ImageButton btn,ImageView imgVVynil, MediaPlayer media, Context ctx) {
        if(btnInstance==null){
            btnInstance=new PlayButton(btn,imgVVynil,media,ctx);
        }
        return btnInstance;
    }

    public static void releaseMediaPlayer(){
        btnInstance.mediaPlayer.release();
    }

    public void playSong(Context context){
        btnInstance.mediaPlayer.start();
        btnPlayPause.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pause,null));
    }

    public void pauseSong(Context context) {
        btnInstance.mediaPlayer.pause();
        btnPlayPause.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.play,null));
    }

    public void stopSong() {
        btnInstance.mediaPlayer.stop();
    }

    public void changeMediaPlayer(MediaPlayer media){
        PlayButton.releaseMediaPlayer();
        this.mediaPlayer=media;

    }

    public MediaPlayer getMediaPlayer() {
        return btnInstance.mediaPlayer;
    }

    public boolean isPlaying() {
        return btnInstance.isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
