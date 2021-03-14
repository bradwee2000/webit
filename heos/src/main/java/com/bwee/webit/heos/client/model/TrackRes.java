package com.bwee.webit.heos.client.model;

import lombok.Data;

import javax.sound.midi.Track;

@Data
public class TrackRes {
    private final String id;
    private final String title;
    private final String artist;
    private final String albumId;
    private final String albumName;
    private final String imageUrl;
    private final Long durationMillis;
}
