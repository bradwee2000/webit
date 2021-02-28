package com.bwee.webit.heos;

import com.bwee.webit.heos.commands.BrowseCommands;
import com.bwee.webit.heos.commands.SystemCommands;
import com.bwee.webit.heos.model.Account;
import com.bwee.webit.heos.model.MusicSource;
import com.bwee.webit.heos.model.Player;
import com.bwee.webit.heos.connect.HeosClient;
import com.bwee.webit.heos.connect.Response;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class HeosSystemService {
    private static final Type MUSIC_SOURCE_RESPONSE_TYPE = new TypeToken<Response<List<MusicSource>>>() {}.getType();

    @Autowired
    private HeosClient heosClient;

    public boolean accountSignIn(final String username, final String password) {
        final Response response = heosClient.execute(SystemCommands.ACCOUNT_SIGN_IN(username, password));
        return response.isSuccess();
    }

    public boolean accountSignOut(){
        final Response response = heosClient.execute(SystemCommands.ACCOUNT_SIGN_OUT);
        return response.isSuccess() && response.getMessage().equals("signed_out");
    }

    public Optional<Account> accountCheck(){
        final Response response = heosClient.execute(SystemCommands.ACCOUNT_CHECK);

        if(response.isSuccess()){
            if(response.getMessage().contains("signed_in")){
                String username = response.getMessage().substring(response.getMessage().indexOf("un=") + 3);
                return Optional.of(new Account(username));
            }
        }
        return Optional.empty();
    }

    public boolean systemHeartBeat(){
        final Response response = heosClient.execute(SystemCommands.HEARTBEAT);
        return response.isSuccess();
    }

    public List<Player> getPlayers(){
        Response response = heosClient.execute(SystemCommands.GET_PLAYERS);

        if(response.isSuccess()){
            List<Player> players = new ArrayList<>();
            for (final Map<String, Object> map : (List<Map<String, Object>>) response.getPayload() ) {
                players.add(new Player(map));
            }
            return players;
        }
        return Collections.emptyList();
    }

    public List<MusicSource> getMusicSources() {
        final Response<List<MusicSource>> response =
                heosClient.execute(BrowseCommands.GET_MUSIC_SOURCES(), MUSIC_SOURCE_RESPONSE_TYPE);
        return response.isSuccess() ? response.getPayload() : Collections.emptyList();
    }
}
