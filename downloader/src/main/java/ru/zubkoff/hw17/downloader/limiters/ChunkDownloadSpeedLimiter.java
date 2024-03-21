package ru.zubkoff.hw17.downloader.limiters;

import java.util.function.Supplier;

public class ChunkDownloadSpeedLimiter implements Limiter<byte[]> {

  private final long speedLimit;

  public ChunkDownloadSpeedLimiter(long speedLimit) {
    this.speedLimit = speedLimit;
  }

  public long getSpeedLimit() {
    return speedLimit;
  }

  @Override
  public byte[] performLimited(Supplier<byte[]> action) throws InterruptedException {

    long startTime = System.currentTimeMillis(); 
    byte[] result = action.get();
    long elapsedTime = System.currentTimeMillis() - startTime; 

    var speed = result.length / Math.max(1, elapsedTime / 1000);
    Thread.sleep(Math.max(0, 1000 * ((speed / speedLimit) - 1)));
    return result;
  }

}