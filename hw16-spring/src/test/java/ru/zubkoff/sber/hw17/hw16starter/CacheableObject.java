package ru.zubkoff.sber.hw17.hw16starter;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import ru.zubkoff.sber.hw17.hw16starter.cache.Cacheable;
import ru.zubkoff.sber.hw17.hw16starter.cache.providers.HashMapCache;

public class CacheableObject {

  static Duration sleepTime = Duration.ofSeconds(3);

  @Cacheable(HashMapCache.class)
  int longComputation(int a) throws InterruptedException {
    TimeUnit.SECONDS.sleep(sleepTime.toSeconds());
    return a + 1;
  }

}
