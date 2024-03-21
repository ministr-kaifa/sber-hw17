package ru.zubkoff.hw17.downloader.limiters;

import java.util.function.Supplier;

public interface Limiter<T> {
  T performLimited(Supplier<T> action) throws InterruptedException;
}