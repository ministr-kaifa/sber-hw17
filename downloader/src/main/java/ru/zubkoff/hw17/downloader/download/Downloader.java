package ru.zubkoff.hw17.downloader.download;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Downloader {
  byte[] download(String targetUrl) throws URISyntaxException, IOException, InterruptedException;
}
