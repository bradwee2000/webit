package com.bwee.webit.service;

import com.bwee.webit.model.YoutubeVideo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteSource;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Service
@Slf4j
public class YoutubeParser {

    private static final String SPACE = " ";
    private static final String FORMAT = "-f best[ext=mp4][height<740]/best[height<=740]/[height<=740]/best";
    private static final String PRINT_JSON = "--print-json";
    private static final String USE_ID_FILENAME = "--id";
    private static final String WRITE_THUMBNAIL = "--write-thumbnail";
    private static final String SKIP_DOWNLOAD = "--skip-download ";

    @Autowired
    private ObjectMapper om;

    @SneakyThrows
    public YoutubeVideo parseVideo(final String id, final boolean isDownload) {
        final String skipDownload = isDownload ? WRITE_THUMBNAIL : SKIP_DOWNLOAD;
        final String cmd = new StringBuilder().append("youtube-dl")
                .append(SPACE).append(USE_ID_FILENAME)
                .append(SPACE).append(PRINT_JSON)
                .append(SPACE).append(FORMAT)
                .append(SPACE).append(skipDownload)
                .append(SPACE).append(id)
                .toString();

        log.info("Running command: {}", cmd);

        final Runtime run = Runtime.getRuntime();
        final Process pr = run.exec(cmd);
        final BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = br.readLine();

        int resultStatus = pr.waitFor();
        if (resultStatus == 1) {
            logError(pr.getErrorStream());
        }

        final YoutubeVideoData data = om.readValue(line, YoutubeVideoData.class);

        return new YoutubeVideo()
                .setId(data.getId())
                .setAuthor(data.getUploader())
                .setTitle(data.getFulltitle())
                .setDescription(data.getDescription())
                .setSourceUrl(data.getUrl())
                .setCategories(data.getCategories())
                .setTags(data.getTags())
                .setDuration(data.getDuration())
                .setWidth(data.getWidth())
                .setHeight(data.getHeight())
                .setExt(data.getExt())
                .setTimestamp(data.getTimestamp())
                .setSize(data.getFilesize());
    }

    @SneakyThrows
    private void logError(final InputStream is) {
        final ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() {
                return is;
            }
        };
        final String error = byteSource.asCharSource(Charset.defaultCharset()).read();
        log.error("{}", error);
    }

    @Data
    public static class YoutubeVideoData {
        private String id;
        private String uploader;
        private String title;
        private String fulltitle;
        private String ext;
        private String url;
        private Long timestamp;
        private Long width;
        private Long height;
        private Long duration;
        private Long filesize;
        private List<Thumbnail> thumbnails;
        private String description;
        private List<String> categories;
        private List<String> tags;
    }

    @Data
    public static class Thumbnail {
        private String url;
        private Long width;
        private Long height;
        private String id;
    }
}
