package com.ase.aplicatiemuzicamobila.AudioListRecycleView;

import android.content.Context;
import android.media.AudioMetadata;
import android.media.AudioMetadataMap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatiemuzicamobila.R;
import com.ase.aplicatiemuzicamobila.StorageClasses.AudioListElement;

public class AudioListViewHolder  extends RecyclerView.ViewHolder {
    TextView tvSongTitle;
    TextView tvSongArtist;
    Context context;

    public AudioListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSongTitle=itemView.findViewById(R.id.tvSongTitle);
        tvSongArtist=itemView.findViewById(R.id.tvArtistName);
        this.context= itemView.getContext();
    }

    public void bind(AudioListElement audioElement, int position){

        MediaMetadataRetriever dataRetriever=new MediaMetadataRetriever();
        dataRetriever.setDataSource(this.context,audioElement.getFileUri());
        audioElement.setFileName(audioElement.getFileName().replace(".mp3"," ").replace(".flac"," "));
        if(audioElement.getArtistName()==null){
            audioElement.setArtistName(dataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }
        this.tvSongTitle.setText(audioElement.getFileName());
        this.tvSongArtist.setText(audioElement.getArtistName());
    }

    public static AudioListViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_audio_element,parent,false);
        return new AudioListViewHolder (view);
    }
}
