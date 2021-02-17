package com.bwee.webit.server.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ImportAlbum {
    private String path;
    private String name;
    private boolean userFilenameAsTrackNum = false;
    private int year;
    private List<String> tags;
    private boolean copyFiles = false;
}
