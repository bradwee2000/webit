package com.bwee.webit.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MusicUserServiceTest {

    @Test
    public void test() {

      int size = 5;
      int cur = 3;
      for (int i = 0; i <= 10; i++) {

          cur -= 1;

          log.info("LALALA: {}", cur);
      }
    }
}