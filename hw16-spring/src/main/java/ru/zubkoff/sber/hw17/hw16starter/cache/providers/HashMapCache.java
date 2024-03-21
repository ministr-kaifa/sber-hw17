package ru.zubkoff.sber.hw17.hw16starter.cache.providers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

@Component
public class HashMapCache implements Cache {
  private final Map<Method, Map<List<Object>, Object>> cacheMap;

  @Override
  public Object evaluateIfAbsent(Method method, List<Object> keyArgs, Supplier<Object> resultEvaluation) {
    return cacheMap.computeIfAbsent(method, newCachedMethod -> new HashMap<>())
      .computeIfAbsent(List.of(keyArgs), firstlyCalledArgs -> resultEvaluation.get());
  }

  public HashMapCache() {
    cacheMap = new HashMap<>();
  }
}
