package com.bwee.webit.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayTrackCodeService {

    @Autowired
    private PlayCodeGenerator playCodeGenerator;

    public boolean isValid(final String trackId, final String playToken) {
        return StringUtils.equals(playToken, toPlayToken(getPlayCode(), trackId))
                || StringUtils.equals(playToken, toPlayToken(getPrevPlayCode(), trackId));
    }

    public String getPlayCode() {
        return playCodeGenerator.generateCode();
    }

    public String getPrevPlayCode() {
        return playCodeGenerator.previousCode();
    }

    public String toPlayToken(final String playKey, final String trackId) {
        final String hashKey = playKey + ":" + trackId;
        final String playToken = hashKey.hashCode() + "";
        return playToken;
    }
}
