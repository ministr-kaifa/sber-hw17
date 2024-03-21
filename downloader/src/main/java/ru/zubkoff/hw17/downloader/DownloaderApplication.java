package ru.zubkoff.hw17.downloader;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.zubkoff.hw17.downloader.download.DownloadManager;

@SpringBootApplication

public class DownloaderApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(DownloaderApplication.class);

  @Autowired
  private DownloadManager downloadManager;

  @Value("${sources}")
  private Path downloadSourcesFile;

  @Override
  public void run(String... args) throws Exception {
    var sources = Files.readAllLines(downloadSourcesFile);
    downloadManager.downloadAll(sources);
    downloadManager.waitForAllDownloadsComplete();
    log.info("all files downloaded");
  }

	public static void main(String[] args) {
    SpringApplication.run(DownloaderApplication.class, 
      "--download.speed=10240",
      "--thread.count=5",
      "--sources=./sources.txt",
      "--destination=./downloads/"
    );
	}

}
