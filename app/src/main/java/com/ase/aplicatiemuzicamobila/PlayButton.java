package com.ase.aplicatiemuzicamobila;


import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.res.ResourcesCompat;

public class PlayButton {
    private boolean isPlaying;
    private static PlayButton btnInstance=null;
    private ImageButton btnPlayPause;
    private MediaPlayer mediaPlayer;
    private Context context;

    private PlayButton() {
    }

    private PlayButton(ImageButton btn, MediaPlayer media,Context ctx){
        this.btnPlayPause=btn;
        this.isPlaying=false;
        this.mediaPlayer=media;
        this.context=ctx;

        this.btnPlayPause.setOnClickListener(v -> {
            if(isPlaying){
                pauseSong(this.context);
            }else{
                playSong(this.context);
            }
            isPlaying=!isPlaying;
        });
    }

    public static PlayButton getPlayPauseInstance(ImageButton btn, MediaPlayer media, Context ctx) {
        if(btnInstance==null){
            btnInstance=new PlayButton(btn,media,ctx);
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
