package ru.zubkoff.hw17.downloader.config;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.zubkoff.hw17.downloader.download.DownloadManager;
import ru.zubkoff.hw17.downloader.download.Downloader;
import ru.zubkoff.hw17.downloader.download.SpeedLimitedChunkedFileDownloader;
import ru.zubkoff.hw17.downloader.limiters.ChunkDownloadSpeedLimiter;

@Configuration
@ConfigurationProperties(prefix = "downloader")
public class DownloaderApplicationConfiguration {
  
  private long defaultSpeedLimitKb;

  @Value("${download.speed:#{null}}")
  private Optional<Long> downloadSpeed;

  @Value("${thread.count:#{null}}")
  private Optional<Integer> threadCount;

  @Value("${destination}")
  private Path destination;

  @Bean
  ChunkDownloadSpeedLimiter limiter() {
    return new ChunkDownloadSpeedLimiter(downloadSpeed.orElse(defaultSpeedLimitKb * 1024));
  }

  @Bean
  SpeedLimitedChunkedFileDownloader downloader(ChunkDownloadSpeedLimiter limiter) {
    return new SpeedLimitedChunkedFileDownloader(limiter);
  }

  @Bean
  DownloadManager downloadManager(Downloader downloader) {

    ExecutorService executor;
    if(threadCount.isPresent()) {
      executor = Executors.newFixedThreadPool(threadCount.get());
    } else {
      executor = Executors.newVirtualThreadPerTaskExecutor();
    }
    
    return new DownloadManager(executor, destination, downloader);
  }

  public long getDefaultSpeedLimitKb() {
    return defaultSpeedLimitKb;
  }

  public void setDefaultSpeedLimitKb(long defaultSpeedLimitKb) {
    this.defaultSpeedLimitKb = defaultSpeedLimitKb;
  }

}
