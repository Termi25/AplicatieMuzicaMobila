package com.ase.aplicatiemuzicamobila.StorageClasses;

import android.net.Uri;


public class AudioListElement {
    private Uri fileUri;
    private String fileName;
    private String artistName;
    private int mimeType;

    private AudioListElement(){}

    public AudioListElement(Uri fileUri, String fileName,int mimeType) {
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getMimeType() {
        return mimeType;
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "AudioListElement{" +
                "fileUri=" + fileUri +
                ", fileName='" + fileName + '\'' +
                ", mimeType=" + mimeType +
                '}';
    }
}


