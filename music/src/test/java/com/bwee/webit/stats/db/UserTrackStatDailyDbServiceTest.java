//package com.bwee.webit.stats.db;
//
//import com.bwee.webit.datasource.EmbeddedCassandraTest;
//import com.bwee.webit.stats.model.StatDuration;
//import com.bwee.webit.stats.model.UserTrackStat;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.time.Clock;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ContextConfiguration(classes = { UserTrackStatDailyDbService.class, UserTrackStatDailyDbServiceTest.Ctx.class })
//class UserTrackStatDailyDbServiceTest extends EmbeddedCassandraTest {
//
//    private static final LocalDateTime now = LocalDateTime.of(2021, 1, 1, 12, 13, 14);
//
//    @Autowired
//    private UserTrackStatDailyDbService dbService;
//
//    private UserTrackStat stat1, stat2, stat3, stat4, stat5;
//
//    @BeforeEach
//    public void before() {
//        stat1 = new UserTrackStat()
//                .setCount(3)
//                .setDuration(StatDuration.Daily)
//                .setTime(now.minusSeconds(1))
//                .setTrackId("ABC")
//                .setUserId("JOHN");
//
//        stat2 = new UserTrackStat()
//                .setCount(2)
//                .setDuration(StatDuration.Daily)
//                .setTime(now)
//                .setTrackId("ABC")
//                .setUserId("JOHN");
//
//        stat3 = new UserTrackStat()
//                .setCount(2)
//                .setDuration(StatDuration.Daily)
//                .setTime(now)
//                .setTrackId("ABC")
//                .setUserId("MARY");
//
//        stat4 = new UserTrackStat()
//                .setCount(4)
//                .setDuration(StatDuration.Daily)
//                .setTime(now.minusDays(1))
//                .setTrackId("ABC")
//                .setUserId("JOHN");
//
//        stat5 = new UserTrackStat()
//                .setCount(5)
//                .setDuration(StatDuration.Daily)
//                .setTime(now.minusDays(2))
//                .setTrackId("DEF")
//                .setUserId("JOHN");
//
//        dbService.deleteAll(stat1, stat2, stat3, stat4, stat5);
//    }
//
//    @Test
//    public void testSaveAndFindById_shouldReturnEqualResult() {
//        try {
//            dbService.saveAll(stat1, stat2);
//            assertThat(dbService.findByUserId(stat1.getUserId(), 1)).containsExactly(stat2, stat1);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testFindByUserId_shouldReturnAllUserRelatedRecords() {
//        dbService.saveAll(stat1, stat2, stat3);
//        assertThat(dbService.findByUserId("JOHN", 1)).containsExactly(stat2, stat1);
//        assertThat(dbService.findByUserId("MARY", 1)).containsExactly(stat3);
//    }
//
//    public static class Ctx {
//        @Bean
//        public Clock clock() {
//            return Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
//        }
//    }
//}