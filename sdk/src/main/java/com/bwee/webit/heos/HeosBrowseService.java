package com.bwee.webit.heos;

import com.bwee.webit.heos.commands.BrowseCommands;
import com.bwee.webit.heos.model.MusicSource;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.connect.Response;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Slf4j
@Service
public class HeosBrowseService {

    private static final Type MEDIA_TYPE = new TypeToken<Response<List<MusicSource.Media>>>() {}.getType();

    @Autowired
    private HeosClient heosClient;

    public List<MusicSource.Media> browseSource(final String sourceId) {
        final Response<List<MusicSource.Media>> response =
                heosClient.execute(BrowseCommands.BROWSE_SOURCE(sourceId), MEDIA_TYPE);

        if (!response.isSuccess()) {
            log.error("Failed to browse sid={}. Error={}", sourceId, response.getMessage());
            return emptyList();
        }
        return response.getPayload().stream()
                .peek(m -> m.setSid(sourceId))
                .collect(Collectors.toList());
    }

    public List<MusicSource.Media> browseContainer(final String sourceId, final String containerId) {
        final Response<List<MusicSource.Media>> response =
                heosClient.execute(BrowseCommands.BROWSE_SOURCE_CONTAINER(sourceId, containerId), MEDIA_TYPE);

        if (!response.isSuccess()) {
            log.error("Failed to browse sid={} cid={}. Error={}", sourceId, containerId, response.getMessage());
            return emptyList();
        }
        return response.getPayload().stream()
                .peek(m -> m.setSid(sourceId))
                .collect(Collectors.toList());
    }
}
