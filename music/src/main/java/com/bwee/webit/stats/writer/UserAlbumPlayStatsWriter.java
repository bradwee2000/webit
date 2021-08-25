package com.bwee.webit.stats.writer;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.map.MapWriter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class UserAlbumPlayStatsWriter implements MapWriter<Long, String> {

    @Override
    public void write(final Map<Long, String> map) {
        log.info("YAN MAGWRITE NA DAW! {}", map);
    }

    @Override
    public void delete(Collection<Long> keys) {

    }
}
