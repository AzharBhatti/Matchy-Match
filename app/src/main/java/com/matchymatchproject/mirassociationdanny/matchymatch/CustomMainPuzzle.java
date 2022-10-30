package com.matchymatchproject.mirassociationdanny.matchymatch;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class CustomMainPuzzle {
    private String voice;

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    //    private ArrayList<byte[]> playableImages;
    public CustomMainPuzzle(){}

    public CustomMainPuzzle(String voice, String image) {
        this.voice = voice;
        this.image = image;
//        this.playableImages = playableImages;
    }

}
