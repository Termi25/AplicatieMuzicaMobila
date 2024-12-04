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

    public AudioListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSongTitle=itemView.findViewById(R.id.tvSongTitle);
    }

    public void bind(AudioListElement audioElement){
        this.tvSongTitle.setText(audioElement.getSongTitle());
    }

    public static AudioListViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_songlist,parent,false);
        return new AudioListViewHolder (view);
    }
}
