package com.bwee.webit.service;

import com.bwee.webit.exception.AlbumNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(AlbumImportCacheService.class)
class AlbumImportCacheServiceTest {

    @MockBean
    private AlbumService albumService;

    @Autowired
    private AlbumImportCacheService albumImportCacheService;

    @Test
    public void testGetWithNonExistingAlbum_shouldReturnEmpty() {
        when(albumService.findByIdStrict("ABC")).thenThrow(new AlbumNotFoundException("ABC"));
        assertThat(albumImportCacheService.get("ABC")).isEmpty();
    }
}