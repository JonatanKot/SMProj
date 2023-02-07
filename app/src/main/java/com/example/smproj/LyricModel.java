package com.example.smproj;

import java.io.Serializable;

public class LyricModel implements Serializable {
    int trackId;
    String lyricChecksum;
    int lyricId;
    String songUrl;
    String artistUrl;
    String artist;
    String song;
    int songRank;

    public LyricModel(String lyricChecksum, int trackId, int lyricId, String songUrl, String artistUrl,
                      String artist, String song, int songRank) {
        this.lyricChecksum= lyricChecksum;
        this.trackId = trackId;
        this.lyricId = lyricId;
        this.songUrl = songUrl;
        this.artistUrl = artistUrl;
        this.artist = artist;
        this.song = song;
        this.songRank = songRank;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getLyricChecksum() {
        return lyricChecksum;
    }

    public void setLyricChecksum(String lyricChecksum) {
        this.lyricChecksum = lyricChecksum;
    }

    public int getLyricId() {
        return lyricId;
    }

    public void setLyricId(int lyricId) {
        this.lyricId = lyricId;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getArtistUrl() {
        return artistUrl;
    }

    public void setArtistUrl(String artistUrl) {
        this.artistUrl = artistUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public int getSongRank() {
        return songRank;
    }

    public void setSongRank(int songRank) {
        this.songRank = songRank;
    }
}