package com.ase.aplicatiemuzicamobila.AudioListRecycleView;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ase.aplicatiemuzicamobila.StorageClasses.AudioListElement;

import java.util.ArrayList;
import java.util.List;

public class AdapterAudioList extends ListAdapter<AudioListElement, AudioListViewHolder> {

    private AudioListViewHolder AudioListViewHolder;
    private List<AudioListElement> listaAudioElements=new ArrayList<>();

    public AdapterAudioList(@NonNull DiffUtil.ItemCallback<AudioListElement> diffCallback) {
        super(diffCallback);
    }

    @Override
    public AudioListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return AudioListViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioListViewHolder holder, int position) {
        AudioListElement curenta=getItem(position);
        holder.bind(curenta);
    }

    public static class AudioElementDiff extends DiffUtil.ItemCallback<AudioListElement>{

        @Override
        public boolean areItemsTheSame(@NonNull AudioListElement oldItem, @NonNull AudioListElement newItem) {
            return oldItem.getFileUri().equals(newItem.getFileUri());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AudioListElement oldItem, @NonNull AudioListElement newItem) {
            return oldItem.getFileName().equals(newItem.getFileName());
        }
    }
}