package ru.zubkoff.sber.hw17.hw16starter.cache;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import ru.zubkoff.sber.hw17.hw16starter.cache.providers.Cache;

@Component
public class CacheResolver {

  private final Map<Class<? extends Cache>, Cache> caches;

  public CacheResolver(Cache... caches) {
    this.caches = Stream.of(caches)
      .collect(Collectors.toMap(Cache::getClass, cache -> cache));
  }

  public Cache resolve(Cacheable cacheInfo) {
    var cacheClazz = cacheInfo.value();
    if(!caches.containsKey(cacheClazz)) {
      throw new NoSuchElementException("No such cache in resolver");
    }
    return caches.get(cacheClazz);
  }
  
}
