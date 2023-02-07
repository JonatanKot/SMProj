package com.example.smproj;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static Boolean releaseInstance(){
        if(instance != null){
            instance.release();
            instance = null;
            return true;
        }
        return false;
    }

    public static int currentIndex = -1;
    //public static AudioModel impulseResp = null;
}
