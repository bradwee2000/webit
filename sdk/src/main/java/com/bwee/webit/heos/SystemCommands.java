package com.bwee.webit.heos;

public class SystemCommands {

    public static final String HEARTBEAT = "heos://system/heart_beat";
    public static final String GET_PLAYERS = "heos://player/get_players";
    public static final String ACCOUNT_CHECK = "heos://system/check_account";
    public static final String ACCOUNT_SIGN_OUT = "heos://system/sign_out";

    public static final String ACCOUNT_SIGN_IN(String username, String password){
        return "heos://system/sign_in?un=" + username + "&pw=" + password;
    }

    public static final String REGISTER_FOR_CHANGE_EVENTS(boolean enabled){
        if(enabled){
            return  "heos://system/register_for_change_events?enable=on";
        }
        return  "heos://system/register_for_change_events?enable=off";
    }
}
