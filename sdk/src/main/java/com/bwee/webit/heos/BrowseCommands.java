package com.bwee.webit.heos;

public class BrowseCommands {

    public static String GET_MUSIC_SOURCES() {
        return "heos://browse/get_music_sources";
    }

    public static String PLAY_URL(String pid, String url) {
        return "heos://browse/play_stream?pid=" + pid + "&url=" + url;
    }

    public static String BROWSE_SOURCE(String sid) {
        return "heos://browse/browse?sid=" + sid;
    }

    public static String BROWSE_SOURCE_CONTAINER(String sid, String cid) {
        return "heos://browse/browse?sid=" + sid + "&cid=" + cid;
    }

    public static String ADD_CONTAINER_TO_QUEUE(String pid, String sid, String cid, AddCriteria addCriteria) {
        return CommandBuilder.of("heos://browse/add_to_queue")
                .param("pid", pid)
                .param("sid", sid)
                .param("cid", cid)
                .param("aid", addCriteria.getCode())
                .build();
    }

    public static String ADD_TRACK_TO_QUEUE(String pid, String sid, String cid, String mid, AddCriteria addCriteria) {
        return CommandBuilder.of("heos://browse/add_to_queue")
                .param("pid", pid)
                .param("sid", sid)
                .param("cid", cid)
                .param("mid", mid)
                .param("aid", addCriteria.getCode())
                .build();
    }
}
