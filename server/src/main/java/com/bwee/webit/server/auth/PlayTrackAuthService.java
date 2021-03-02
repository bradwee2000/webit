package com.bwee.webit.server.auth;

import com.bwee.webit.server.exception.UnauthorizedAccessException;
import com.bwee.webit.service.PlayCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PlayTrackAuthService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PlayCodeGenerator playCodeGenerator;

    public void validate(final String trackId, final String playToken) {
        final String expectedToken = toPlayToken(getPlayCode(), trackId);

        // If token does not match w/ current and previous token, throw exception
        if (!StringUtils.equals(playToken, expectedToken)
                && !StringUtils.equals(playToken, toPlayToken(getPrevPlayCode(), trackId))) {
            throw new UnauthorizedAccessException(request);
        }
    }

    public String getPlayCode() {
        return playCodeGenerator.generateCode();
    }

    public String getPrevPlayCode() {
        return playCodeGenerator.previousCode();
    }

    private String toPlayToken(final String playKey, final String trackId) {
        final String hashKey = playKey + ":" + trackId;
        final String playToken = hashKey.hashCode() + "";
        return playToken;
    }
}
