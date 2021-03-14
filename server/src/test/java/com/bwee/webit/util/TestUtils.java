package com.bwee.webit.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    private static final ObjectMapper OM = new ObjectMapper();

    public static ObjectMapper om() {
        return OM;
    }
}
