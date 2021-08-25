package com.bwee.webit.service;

import com.mpatric.mp3agic.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ID3 implements ID3v2 {
    private final Optional<ID3v2> id3v2;
    private final Optional<ID3v1> id3v1;

    public ID3(ID3v2 id3v2, ID3v1 id3v1) {
        this.id3v2 = Optional.ofNullable(id3v2);
        this.id3v1 = Optional.ofNullable(id3v1);
    }

    @Override
    public boolean getPadding() {
        return id3v2.map(v -> v.getPadding()).orElse(Boolean.FALSE);
    }

    @Override
    public void setPadding(boolean b) {
        id3v2.ifPresent(v -> v.setPadding(b));
    }

    @Override
    public boolean hasFooter() {
        return id3v2.map(v -> v.hasFooter()).orElse(Boolean.FALSE);
    }

    @Override
    public void setFooter(boolean b) {
        id3v2.ifPresent(v -> v.setFooter(b));
    }

    @Override
    public boolean hasUnsynchronisation() {
        return id3v2.map(v -> v.hasUnsynchronisation()).orElse(Boolean.FALSE);
    }

    @Override
    public void setUnsynchronisation(boolean b) {
        id3v2.ifPresent(v -> v.setUnsynchronisation(b));
    }

    @Override
    public int getBPM() {
        return id3v2.map(v -> v.getBPM()).orElse(0);
    }

    @Override
    public void setBPM(int i) {
        id3v2.ifPresent(v -> v.setBPM(i));
    }

    @Override
    public String getGrouping() {
        return id3v2.map(v -> v.getGrouping()).orElse(null);
    }

    @Override
    public void setGrouping(String s) {
        id3v2.ifPresent(v -> v.setGrouping(s));
    }

    @Override
    public String getKey() {
        return id3v2.map(v -> v.getKey()).orElse(null);
    }

    @Override
    public void setKey(String s) {
        id3v2.ifPresent(v -> v.setKey(s));
    }

    @Override
    public String getDate() {
        return id3v2.map(v -> v.getDate()).orElse(null);
    }

    @Override
    public void setDate(String s) {
        id3v2.ifPresent(v -> v.setDate(s));
    }

    @Override
    public String getComposer() {
        return id3v2.map(v -> v.getComposer()).orElse(null);
    }

    @Override
    public void setComposer(String s) {
        id3v2.ifPresent(v -> v.setComposer(s));
    }

    @Override
    public String getPublisher() {
        return id3v2.map(v -> v.getPublisher()).orElse(null);
    }

    @Override
    public void setPublisher(String s) {
        id3v2.ifPresent(v -> v.setPublisher(s));
    }

    @Override
    public String getOriginalArtist() {
        return id3v2.map(v -> v.getOriginalArtist()).orElse(null);
    }

    @Override
    public void setOriginalArtist(String s) {
        id3v2.ifPresent(v -> v.setOriginalArtist(s));
    }

    @Override
    public String getAlbumArtist() {
        return id3v2.map(v -> v.getAlbumArtist()).orElse(null);
    }

    @Override
    public void setAlbumArtist(String s) {
        id3v2.ifPresent(v -> v.setAlbumArtist(s));
    }

    @Override
    public String getCopyright() {
        return id3v2.map(v -> v.getCopyright()).orElse(null);
    }

    @Override
    public void setCopyright(String s) {
        id3v2.ifPresent(v -> v.setCopyright(s));
    }

    @Override
    public String getArtistUrl() {
        return id3v2.map(v -> v.getArtistUrl()).orElse(null);
    }

    @Override
    public void setArtistUrl(String s) {
        id3v2.ifPresent(v -> v.setArtistUrl(s));
    }

    @Override
    public String getCommercialUrl() {
        return id3v2.map(v -> v.getCommercialUrl()).orElse(null);
    }

    @Override
    public void setCommercialUrl(String s) {
        id3v2.ifPresent(v -> v.setCommercialUrl(s));
    }

    @Override
    public String getCopyrightUrl() {
        return id3v2.map(v -> v.getCopyrightUrl()).orElse(null);
    }

    @Override
    public void setCopyrightUrl(String s) {
        id3v2.ifPresent(v -> v.setCopyrightUrl(s));
    }

    @Override
    public String getAudiofileUrl() {
        return id3v2.map(v -> v.getAudiofileUrl()).orElse(null);
    }

    @Override
    public void setAudiofileUrl(String s) {
        id3v2.ifPresent(v -> v.setAudiofileUrl(s));
    }

    @Override
    public String getAudioSourceUrl() {
        return id3v2.map(v -> v.getAudioSourceUrl()).orElse(null);
    }

    @Override
    public void setAudioSourceUrl(String s) {
        id3v2.ifPresent(v -> v.setAudioSourceUrl(s));
    }

    @Override
    public String getRadiostationUrl() {
        return id3v2.map(v -> v.getRadiostationUrl()).orElse(null);
    }

    @Override
    public void setRadiostationUrl(String s) {
        id3v2.ifPresent(v -> v.setRadiostationUrl(s));
    }

    @Override
    public String getPaymentUrl() {
        return id3v2.map(v -> v.getPaymentUrl()).orElse(null);
    }

    @Override
    public void setPaymentUrl(String s) {
        id3v2.ifPresent(v -> v.setPaymentUrl(s));
    }

    @Override
    public String getPublisherUrl() {
        return id3v2.map(v -> v.getPublisherUrl()).orElse(null);
    }

    @Override
    public void setPublisherUrl(String s) {
        id3v2.ifPresent(v -> v.setPublisherUrl(s));
    }

    @Override
    public String getUrl() {
        return id3v2.map(v -> v.getUrl()).orElse(null);
    }

    @Override
    public void setUrl(String s) {
        id3v2.ifPresent(v -> v.setUrl(s));
    }

    @Override
    public String getPartOfSet() {
        return id3v2.map(v -> v.getPartOfSet()).orElse(null);
    }

    @Override
    public void setPartOfSet(String s) {
        id3v2.ifPresent(v -> v.setPartOfSet(s));
    }

    @Override
    public boolean isCompilation() {
        return id3v2.map(v -> v.isCompilation()).orElse(false);
    }

    @Override
    public void setCompilation(boolean b) {
        id3v2.ifPresent(v -> v.setCompilation(b));
    }

    @Override
    public ArrayList<ID3v2ChapterFrameData> getChapters() {
        return id3v2.map(v -> v.getChapters()).orElse(null);
    }

    @Override
    public void setChapters(ArrayList<ID3v2ChapterFrameData> arrayList) {
        id3v2.ifPresent(v -> v.setChapters(arrayList));
    }

    @Override
    public ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC() {
        return id3v2.map(v -> v.getChapterTOC()).orElse(new ArrayList<>(0));
    }

    @Override
    public void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> chapterTocList) {
        id3v2.ifPresent(v -> v.setChapterTOC(chapterTocList));
    }

    @Override
    public String getEncoder() {
        return null;
    }

    @Override
    public void setEncoder(String s) {
        id3v2.ifPresent(v -> v.setEncoder(s));
    }

    @Override
    public byte[] getAlbumImage() {
        return id3v2.map(v -> v.getAlbumImage()).orElse(null);
    }

    @Override
    public void setAlbumImage(byte[] bytes, String s) {
        id3v2.ifPresent(v -> v.setAlbumImage(bytes, s));
    }

    @Override
    public void setAlbumImage(byte[] bytes, String s, byte b, String s1) {
        id3v2.ifPresent(v -> v.setAlbumImage(bytes, s, b, s1));
    }

    @Override
    public void clearAlbumImage() {
        id3v2.ifPresent(v -> v.clearAlbumImage());
    }

    @Override
    public String getAlbumImageMimeType() {
        return id3v2.map(v -> v.getAlbumImageMimeType()).orElse(null);
    }

    @Override
    public int getWmpRating() {
        return id3v2.map(v -> v.getWmpRating()).orElse(0);
    }

    @Override
    public void setWmpRating(int wpmRating) {
        id3v2.ifPresent(v -> v.setWmpRating(wpmRating));
    }

    @Override
    public String getItunesComment() {
        return id3v2.map(v -> v.getItunesComment()).orElse(null);
    }

    @Override
    public void setItunesComment(String itunesComment) {
        id3v2.ifPresent(v -> v.setItunesComment(itunesComment));
    }

    @Override
    public String getLyrics() {
        return id3v2.map(v -> v.getLyrics()).orElse(null);
    }

    @Override
    public void setLyrics(String lyrics) {
        id3v2.ifPresent(v -> v.setLyrics(lyrics));
    }

    @Override
    public void setGenreDescription(String genreDescription) {
        id3v2.ifPresent(v -> v.setGenreDescription(genreDescription));
    }

    @Override
    public int getDataLength() {
        return id3v2.map(v -> v.getDataLength()).orElse(0);
    }

    @Override
    public int getLength() {
        return id3v2.map(v -> v.getLength()).orElse(0);
    }

    @Override
    public boolean getObseleteFormat() {
        return id3v2.map(v -> v.getObseleteFormat()).orElse(false);
    }

    @Override
    public Map<String, ID3v2FrameSet> getFrameSets() {
        return id3v2.map(v -> v.getFrameSets()).orElse(Collections.emptyMap());
    }

    @Override
    public void clearFrameSet(String s) {
        id3v2.ifPresent(v -> v.clearFrameSet(s));
    }

    @Override
    public String getVersion() {
        return getString(ID3v1::getVersion);
    }

    @Override
    public String getTrack() {
        return getString(ID3v1::getTrack);
    }

    @Override
    public void setTrack(String s) {
        id3v2.ifPresent(v -> v.setTrack(s));
        id3v1.ifPresent(v -> v.setTrack(s));
    }

    @Override
    public String getArtist() {
        return getString(ID3v1::getArtist);
    }

    @Override
    public void setArtist(String s) {
        id3v2.ifPresent(v -> v.setArtist(s));
        id3v1.ifPresent(v -> v.setArtist(s));
    }

    @Override
    public String getTitle() {
        return getString(ID3v1::getTitle);
    }

    @Override
    public void setTitle(String s) {
        id3v2.ifPresent(v -> v.setTitle(s));
        id3v1.ifPresent(v -> v.setTitle(s));
    }

    @Override
    public String getAlbum() {
        return getString(ID3v1::getAlbum);
    }

    @Override
    public void setAlbum(String s) {
        id3v2.ifPresent(v -> v.setAlbum(s));
        id3v1.ifPresent(v -> v.setAlbum(s));
    }

    @Override
    public String getYear() {
        return getString(ID3v1::getYear);
    }

    @Override
    public void setYear(String s) {
        id3v2.ifPresent(v -> v.setYear(s));
        id3v1.ifPresent(v -> v.setYear(s));
    }

    @Override
    public int getGenre() {
        return getInt(ID3v1::getGenre);
    }

    @Override
    public void setGenre(int i) {
        id3v2.ifPresent(v -> v.setGenre(i));
        id3v1.ifPresent(v -> v.setGenre(i));
    }

    @Override
    public String getGenreDescription() {
        return getString((id3) ->id3.getGenreDescription());
    }

    @Override
    public String getComment() {
        return getString((id3) ->id3.getComment());
    }

    @Override
    public void setComment(String s) {
        id3v2.ifPresent(v -> v.setComment(s));
        id3v1.ifPresent(v -> v.setComment(s));
    }

    @Override
    public byte[] toBytes() throws NotSupportedException {
        return new byte[0];
    }

    private String getString(final Function<ID3v1, String> func) {
        return id3v2.map(id3 -> func.apply(id3))
                .filter(v -> !StringUtils.isEmpty(v))
                .orElseGet(() -> id3v1.map(id3 -> func.apply(id3)).orElse(null));
    }

    private Integer getInt(final Function<ID3v1, Integer> func) {
        return id3v2.map(id3 -> func.apply(id3))
                .filter(v -> v != null)
                .orElseGet(() -> id3v1.map(id3 -> func.apply(id3)).orElse(0));
    }
}
