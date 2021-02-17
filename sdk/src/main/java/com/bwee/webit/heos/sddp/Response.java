package com.bwee.webit.heos.sddp;

import com.bwee.webit.heos.Results;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Map;

@Slf4j
public class Response<T> {
    private Map<String, String> heos;

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

    public T getPayload() {
        return payload;
    }
}
