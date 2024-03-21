package ru.zubkoff.hw17.downloader.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import ru.zubkoff.hw17.downloader.limiters.ChunkDownloadSpeedLimiter;

public class SpeedLimitedChunkedFileDownloader implements Downloader {
  private final ChunkDownloadSpeedLimiter limiter;

  public SpeedLimitedChunkedFileDownloader(ChunkDownloadSpeedLimiter limiter) {
    this.limiter = limiter;
  }

  public byte[] download(String targetUrl) throws URISyntaxException, IOException, InterruptedException {
    var download = new ChunkedFileDownload(limiter.getSpeedLimit(), targetUrl);
    var contentLength = download.contentLength();
    var outputStream = new ByteArrayOutputStream();

    for (int i = 0; i < contentLength / download.getChunkSize(); i++) {
      byte[] data = limiter.performLimited(() -> {
        try {
          return download.nextChunk();
        } catch (IOException e) {
          throw new RuntimeException(e);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new RuntimeException(e);
        }
      });
      outputStream.write(data);
    }
    return outputStream.toByteArray();
  }

}
