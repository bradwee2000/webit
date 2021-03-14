package com.bwee.webit.exception;

import static com.bwee.webit.constant.YoutubeConstants.YOUTUBE_SOURCE;

public class YoutubeContentNotFoundException extends ContentNotFoundException {

    public YoutubeContentNotFoundException(String id) {
        super(id, YOUTUBE_SOURCE);
    }
}
