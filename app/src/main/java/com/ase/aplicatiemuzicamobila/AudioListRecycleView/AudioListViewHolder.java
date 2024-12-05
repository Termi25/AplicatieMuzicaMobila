package com.ase.aplicatiemuzicamobila.AudioListRecycleView;

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

    public AudioListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSongTitle=itemView.findViewById(R.id.tvSongTitle);
        tvSongArtist=itemView.findViewById(R.id.tvArtistName);
    }

    public void bind(AudioListElement audioElement, int position){
        this.tvSongTitle.setText(audioElement.getFileName().split("\\.")[0]);
        this.tvSongArtist.setText(audioElement.getArtistName());

    }

    public static AudioListViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_audio_element,parent,false);
        return new AudioListViewHolder (view);
    }
}
