package com.trasen.imis.model.resp;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class MusicMessage extends BaseMessage {
    private Music Music;
    /**
     * @return the music
     */
    public Music getMusic() {
        return Music;
    }
    /**
     * @param music the music to set
     */
    public void setMusic(Music music) {
        this.Music = music;
    }
}
