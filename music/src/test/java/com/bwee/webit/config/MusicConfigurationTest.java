package com.bwee.webit.config;

import com.bwee.webit.service.TrackIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MusicConfiguration.class)
class MusicConfigurationTest {

    @Autowired
    private TrackIdGenerator generator;

    @Test
    public void testInit_shouldNotThrowException() {

    }
}