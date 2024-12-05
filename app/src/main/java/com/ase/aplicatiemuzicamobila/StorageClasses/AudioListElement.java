package com.ase.aplicatiemuzicamobila.StorageClasses;

import android.net.Uri;


public class AudioListElement {
    private Uri fileUri;
    private String fileName;
    private int mimeType;
    private AudioListElement beforeCurrentElement;
    private AudioListElement afterCurrentElement;

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

    public AudioListElement getBeforeCurrentElement() {
        return beforeCurrentElement;
    }

    public void setBeforeCurrentElement(AudioListElement beforeCurrentMediaFile) {
        this.beforeCurrentElement = beforeCurrentMediaFile;
    }

    public AudioListElement getAfterCurrentElement() {
        return afterCurrentElement;
    }

    public void setAfterCurrentElement(AudioListElement afterCurrentMediaFile) {
        this.afterCurrentElement = afterCurrentMediaFile;
    }

    @Override
    public String toString() {
        return "AudioListElement{" +
                "fileUri=" + fileUri +
                ", fileName='" + fileName + '\'' +
                ", mimeType=" + mimeType +
                ", beforeCurrentElement=" + beforeCurrentElement +
                ", afterCurrentElement=" + afterCurrentElement +
                '}';
    }
}
