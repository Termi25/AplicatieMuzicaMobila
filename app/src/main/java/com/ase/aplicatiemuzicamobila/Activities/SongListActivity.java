package com.ase.aplicatiemuzicamobila.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatiemuzicamobila.AudioListRecycleView.AdapterAudioList;
import com.ase.aplicatiemuzicamobila.R;

public class SongListActivity extends AppCompatActivity {
    private AdapterAudioList adapterAudio;
    private RecyclerView rvAudiolist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);

        this.rvAudiolist=findViewById(R.id.rvAudioList);
        this.adapterAudio= new AdapterAudioList(new AdapterAudioList.AudioElementDiff());
        this.rvAudiolist.setAdapter(this.adapterAudio);
        this.rvAudiolist.setLayoutManager(new LinearLayoutManager(this));


    }
}
