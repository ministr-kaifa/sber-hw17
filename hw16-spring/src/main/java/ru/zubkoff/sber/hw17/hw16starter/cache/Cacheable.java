package ru.zubkoff.sber.hw17.hw16starter.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.zubkoff.sber.hw17.hw16starter.cache.providers.Cache;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

  Class<? extends Cache> value();

}
