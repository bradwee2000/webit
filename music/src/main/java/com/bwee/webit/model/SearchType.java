package com.bwee.webit.model;

public enum SearchType {

    allFields("all"),
    artist("artist");

    public static SearchType of(final String id) {
        for (SearchType type : SearchType.values()) {
            if (type.id.equalsIgnoreCase(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("AlbumSearchType not found for code: " + id);
    }

    String id;

    SearchType(final String id) {
        this. id = id;
    }

    public String getId() {
        return id;
    }
}
