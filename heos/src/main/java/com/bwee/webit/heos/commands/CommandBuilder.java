package com.bwee.webit.heos.commands;

public class CommandBuilder {

    public static CommandBuilder of(String command) {
        return new CommandBuilder(command);
    }

    private final StringBuilder sb;

    public CommandBuilder(String command) {
        sb = new StringBuilder(command).append("?");
    }

    public CommandBuilder param(String key, String value) {
        sb.append("&").append(key).append("=").append(value);
        return this;
    }

    public CommandBuilder param(String key, int value) {
        return param(key, String.valueOf(value));
    }

    public String build() {
        return sb.toString();
    }
}
