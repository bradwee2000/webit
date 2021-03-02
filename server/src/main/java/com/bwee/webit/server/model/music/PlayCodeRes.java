package com.bwee.webit.server.model.music;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlayCodeRes {

    private String playCode;
}
