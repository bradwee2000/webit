package com.bwee.webit.heos.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Deezer, Amazon, Spotify, Local, etc
 */
@Data
@Slf4j
public class MusicSource {

    private String sid;
    private String imageUrl;
    private String type;
    private Boolean available;
    private String serviceUsername;
    private String name;

    @Data
    public static class Media {

        public static final String YES = "yes";

        private String sid; // source ID
        private String cid; // container ID
        private String mid; // media ID
        private String container; // yes or no
        private String playable; // yes or no.
        private String type; // see MediaType
        private String name;
        private String imageUrl;

        public boolean isPlayable() {
            return YES.equals(playable);
        }

        public boolean isContainer() {
            return YES.equals(container);
        }
    }
}
