package com.bwee.webit.heos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
public class Player {
    private String name;
    private String pid;
    private String model;
    private String version;
    private Double gid;

    /**
     * Constructor to be called from the 'getPlayers' response. (See HeosSystem -> getPlayers)
     * @param map
     */
    public Player(Map<String, Object> map){
        this.name = (String) map.get("name");
        // For some reason pid gets cast to a double by default, leaving a dot (.) in it.
        // For operations, it should just be a String
        this.pid = String.valueOf((Double) map.get("pid")).replace(".", "");
        this.model = (String) map.get("model");
        this.version = (String) map.get("version");

        // Gid is not always contained.
        if (map.containsKey("gid")){
            this.gid = (Double) map.get("gid");
        }
    }
}
