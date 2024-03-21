package ru.zubkoff.sber.hw17.hw16starter.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import ru.zubkoff.sber.hw17.hw16starter.cache.CacheResolver;
import ru.zubkoff.sber.hw17.hw16starter.cache.Cacheable;

@Aspect
@Component
public class CacheableAspect {

  private final CacheResolver cacheResolver;
  
  public CacheableAspect(CacheResolver cacheResolver) {
    this.cacheResolver = cacheResolver;
  }

  @Around("@annotation(ru.zubkoff.sber.hw17.hw16starter.cache.Cacheable)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    return cacheResolver.resolve(method.getAnnotation(Cacheable.class))
      .evaluateIfAbsent(method, List.of(joinPoint.getArgs()), () -> {
        try {
          return joinPoint.proceed();
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      });
  }

}