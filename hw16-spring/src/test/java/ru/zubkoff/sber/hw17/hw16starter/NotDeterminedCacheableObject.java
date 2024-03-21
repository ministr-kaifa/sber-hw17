package ru.zubkoff.sber.hw17.hw16starter;

import ru.zubkoff.sber.hw17.hw16starter.cache.Cacheable;
import ru.zubkoff.sber.hw17.hw16starter.cache.providers.HashMapCache;

public class NotDeterminedCacheableObject {
  static int state = 1;

  @Cacheable(HashMapCache.class)
  int longComputation(int a) throws InterruptedException {
    state++;
    return state;
  }

}
