package ru.zubkoff.sber.hw17.hw16starter.cache.providers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

public interface Cache {
  Object evaluateIfAbsent(Method method, List<Object> args, Supplier<Object> resultEvaluation);
}
