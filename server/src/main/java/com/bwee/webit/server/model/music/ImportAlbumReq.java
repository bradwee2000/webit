package com.bwee.webit.server.model.music;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ImportAlbumReq {
    private String path;
    private String name;
    private boolean userFilenameAsTrackNum = true;
    private Integer year;
    private List<String> tags;
    private boolean copyFiles = false;
    private boolean overwriteCache = false;
}
