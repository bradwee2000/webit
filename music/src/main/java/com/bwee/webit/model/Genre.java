package com.bwee.webit.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Genre {
    UNKNOWN("-1", "Unknown"),
    BLUES("00", "Blues"),
    CLASSIC_ROCK("01", "Classic Rock"),
    COUNTRY("02", "Country"),
    DANCE("03", "Dance"),
    DISCO("04", "Disco"),
    FUNK("05", "Funk"),
    GRUNGE("06", "Grunge"),
    HIP_HOP("07", "Hip-Hop"),
    JAZZ("08", "Jazz"),
    METAL("09", "Metal"),
    NEW_AGE("10", "New Age"),
    OLDIES("11", "Oldies"),
    OTHER("12", "Other"),
    POP("13", "Pop"),
    RHYTHM_BLUES("14", "Rhythm and Blues"),
    RAP("15", "Rap"),
    REGGAE("16", "Reggae"),
    ROCK("17", "Rock"),
    TECHNO("18", "Techno"),
    INDUSTRIAL("19", "Industrial"),

    ALTERNATIVE("20", "Alternative"),
    SKA("21", "Ska"),
    DEATH_METAL("22", "Death Metal"),
    PRANKS("23", "Pranks"),
    SOUNDTRACK("24", "Soundtrack"),
    EURO_TECHNO("25", "Euro-Techno"),
    AMBIENT("26", "Ambient"),
    TRIP_HOP("27", "Trip-Hop"),
    VOCAL("28", "Vocal"),
    JAZZ_FUNK("29", "Jazz & Funk"),
    FUSION("30", "Fusion"),
    TRANCE("31", "Trance"),
    CLASSICAL("32", "Classical"),
    INSTRUMENTAL("33", "Instrumental"),
    ACID("34", "Acid"),
    HOUSE("35", "House"),
    GAME("36", "Game"),
    SOUND_CLIP("37", "Sound Clip"),
    GOSPEL("38", "Gospel"),
    NOISE("39", "Noise"),

    ALTERNATIVE_ROCK("40", "Alternative Rock"),
    BASS("41", "Bass"),
    SOUL("42", "Soul"),
    PUNK("43", "Punk"),
    SPACE("44", "Space"),
    MEDITATIVE("45", "Meditative"),
    INSTRUMENTAL_POP("46", "Instrumental Pop"),
    INSTRUMENTAL_ROCK("47", "Instrumental Rock"),
    ETHNIC("48", "Ethnic"),
    GOTHIC("49", "Gothic"),
    DARKWAVE("50", "Darkwave"),
    TECHNO_INDUSTRIAL("51", "Techno-Industrial"),
    ELECTRONIC("52", "Electronic"),
    POP_FOLK("53", "Pop-Folk"),
    EURODANCE("54", "Eurodance"),
    DREAM("55", "Dream"),
    SOUTHERN_ROCK("56", "Southern Rock"),
    COMEDY("57", "Comedy"),
    CULT("58", "Cult"),
    GANGSTA("59", "Gangsta"),

    TOP_40("60", "Top 40"),
    CHRISTIAN_RAP("61", "Christian Rap"),
    POP_FUNK("62", "Pop/Funk"),
    JUNGLE("63", "Jungle"),
    NATIVE_US("64", "Native US"),
    CABARET("65", "Cabaret"),
    NEW_WAVE("66", "New Wave"),
    PSYCHEDELIC("67", "Psychedelic"),
    RAVE("68", "Rave"),
    SHOW_TUNES("69", "Show Tunes"),
    TRAILER("70", "Trailer"),
    LO_FI("71", "Lo-Fi"),
    TRIBAL("72", "Tribal"),
    ACID_PUNK("73", "Acid Punk"),
    ACID_JAZZ("74", "Acid Jazz"),
    POLKA("75", "Polka"),
    RETRO("76", "Retro"),
    MUSICAL("77", "Musical"),
    ROCK_N_ROLL("78", "Rock & Roll"),
    HARD_ROCK("79", "Hard Rock");

    public static Genre

    of(String code) {
        return Arrays.stream(Genre.values())
                .filter(g -> g.getCode().equals(code) || g.getName().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown genre code: " + code));
    }

    public static Genre of(int code) {
        return of(String.format("%02d", code));
    }

    String code;
    String name;

    Genre(String code, String name) {
        this.code = code;
        this.name = name;
    }
    }
