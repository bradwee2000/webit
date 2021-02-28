package com.bwee.webit.heos.sddp;

import com.bwee.webit.heos.HeosUtil;
import com.bwee.webit.heos.Results;
import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Response<T> {
    private Map<String, String> heos;

    private Map<String, String> messageParams;

    /**
     * Can be either null, a Map or an array of maps.
     */
    private T payload;

    public String getCommand() {
        return heos.get("command");
    }

    public String getResult() {
        return heos.get("result");
    }

    public boolean isSuccess() {
        return Results.SUCCESS.equals(getResult());
    }

    public String getMessage() {
        return heos.get("message");
    }

    public Map<String, String> getMessageParams() {
        if (messageParams == null) {
            final String message = getMessage();
            if (StringUtils.isEmpty(message)) {
                return Collections.emptyMap();
            }
            messageParams = HeosUtil.toMap(message);
        }
        return messageParams;
    }

    public String getMessageParam(final String key) {
        return getMessageParams().get(key);
    }

    public T getPayload() {
        return payload;
    }
}
