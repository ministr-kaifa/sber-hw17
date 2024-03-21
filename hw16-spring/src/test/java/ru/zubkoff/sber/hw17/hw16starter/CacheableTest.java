package ru.zubkoff.sber.hw17.hw16starter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class CacheableTest {

  @TestConfiguration
  static class CacheableTestConfig {
    @Bean
    public CacheableObject cacheableObject() {
      return new CacheableObject();
    }
    @Bean
    public NotDeterminedCacheableObject randomCacheableObject() {
      return new NotDeterminedCacheableObject();
    }
  }

  @Autowired
  CacheableObject obj;

  @Autowired
  NotDeterminedCacheableObject notCacheableObject;

  @Test
  void givenCachedObject_whenCallCachedMethodTwice_thenSecondTimeFaster() throws InterruptedException {
    //given
    int input = 1;

    //when
    obj.longComputation(input);

    //then
    assertTimeout(Duration.ofSeconds(1), () -> obj.longComputation(input));
  }

  //если мы кешируем метод который в теории нельзя закешировать(не детерминированный) то результат должен быть одинаковый хоть и метод точно возвращает разные значения
  @Test
  void givenCachedObject_whenCallCachedMethodTwice_thenSameOutput() throws InterruptedException {
    //given
    int input = 1;

    //when
    var firstResult = obj.longComputation(input);
    var secondResult = obj.longComputation(input);
    
    //then
    assertEquals(firstResult, secondResult);
  }

}
