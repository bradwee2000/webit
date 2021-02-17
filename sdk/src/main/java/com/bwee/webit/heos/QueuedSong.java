package com.bwee.webit.heos;

import lombok.Data;

@Data
public class QueuedSong {
    private String song;
    private String album;
    private String artist;
    private String imageUrl;
    private String qid; // queue ID
    private String mid; // media ID
    private String albumId;
}
