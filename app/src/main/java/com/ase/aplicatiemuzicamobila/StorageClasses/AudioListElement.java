package com.ase.aplicatiemuzicamobila.StorageClasses;

public class AudioListElement {
    private String songTitle;

    public AudioListElement(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
}
