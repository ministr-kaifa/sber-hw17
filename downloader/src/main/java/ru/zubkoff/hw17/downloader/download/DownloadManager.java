package ru.zubkoff.hw17.downloader.download;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DownloadManager {

  private final ExecutorService executor;
  private final Path destination;
  private final Downloader downloader;
  
  private ThreadLocal<List<CompletableFuture<Void>>> tasks = new ThreadLocal<>();

  private static final Logger log = LoggerFactory.getLogger(DownloadManager.class);

  public DownloadManager(ExecutorService executor, Path destination, Downloader downloader) {
    this.destination = destination;
    this.downloader = downloader;
    this.executor = executor;
    tasks.set(new ArrayList<>());
  }

  public void downloadAll(String... sourcesUrls) {
    downloadAll(List.of(sourcesUrls));
  }

  public void downloadAll(List<String> sourcesUrls) {
    sourcesUrls
      .forEach(url -> {
        var task = CompletableFuture.supplyAsync(() -> {
          try {
            return downloader.download(url);
          } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
          } 
        }, executor)
        .thenAccept(data -> {
          try {
            var filePath = Files.write(destination.resolve(url.substring(url.lastIndexOf("/") + 1)), data);
            log.info("file " + filePath + " from " + url + " downloaded");
            System.out.println();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
        tasks.get().add(task);
      });
  }

  public void waitForAllDownloadsComplete() {
    tasks.get().forEach(task -> {
      try {
        task.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }
  
}
