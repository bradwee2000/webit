package com.bwee.webit.heos;

import com.bwee.webit.heos.sddp.HeosClient;
import com.bwee.webit.heos.sddp.Response;
import com.bwee.webit.heos.sddp.TelnetConnection;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HeosBrowseService {

    @Autowired
    private HeosClient heosClient;


}
