package com.example.smproj;

import java.util.ArrayList;

public class SongsList {
    static ArrayList<AudioModel> songsList;

    static ArrayList<AudioModel> getInstance(){
        if (songsList == null){
            songsList = new ArrayList<AudioModel>();
        }
        return songsList;
    }
}
