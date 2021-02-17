package com.bwee.webit.heos;

import com.bwee.webit.heos.sddp.Response;
import com.bwee.webit.heos.sddp.TelnetConnection;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

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

    public List<Media> browse() {
        Type type = new TypeToken<Response<List<Media>>>() {}.getType();
        Response<List<Media>> response = TelnetConnection.execute(BrowseCommands.BROWSE_SOURCE(this.sid), type);
        if (!response.isSuccess()) {
            log.error("Failed to browse name={}. Error={}", name, response.getMessage());
            return emptyList();
        }
        return response.getPayload().stream()
                .peek(m -> m.setSid(this.sid))
                .collect(Collectors.toList());
    }

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

        public List<Media> browse() {
            Type type = new TypeToken<Response<List<MusicSource>>>() {}.getType();
            Response<List<Media>> response = TelnetConnection.execute(BrowseCommands.BROWSE_SOURCE_CONTAINER(this.sid, this.cid), type);
            if (!response.isSuccess()) {
                log.error("Failed to browse name={}. Error={}", name, response.getMessage());
                return emptyList();
            }
            return response.getPayload().stream()
                    .peek(m -> m.setSid(this.sid))
                    .collect(Collectors.toList());
        }

        public boolean addToQueue(Player player, AddCriteria addCriteria) {
            Response response;
            if (StringUtils.isEmpty(mid)) {
                response = TelnetConnection.write(BrowseCommands.ADD_CONTAINER_TO_QUEUE(player.getPid(), sid, cid, addCriteria));
            } else {
                response = TelnetConnection.write(BrowseCommands.ADD_TRACK_TO_QUEUE(player.getPid(), sid, cid, mid, addCriteria));
            }
            return response.isSuccess();
        }
    }

}
