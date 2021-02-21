package com.bwee.webit.server.model.music;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class QueueUpdateReq {

    private List<String> trackIds = Collections.emptyList();
}
