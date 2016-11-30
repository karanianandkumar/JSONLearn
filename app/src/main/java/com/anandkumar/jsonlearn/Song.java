package com.anandkumar.jsonlearn;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Anand on 11/27/2016.
 */

public class Song extends RealmObject{

    @PrimaryKey
    private String name;

    private String index,lyric,audio,video;


    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
