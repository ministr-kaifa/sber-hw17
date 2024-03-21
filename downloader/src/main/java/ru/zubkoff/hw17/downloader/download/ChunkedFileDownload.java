package ru.zubkoff.hw17.downloader.download;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChunkedFileDownload {

  private static final Logger log = LoggerFactory.getLogger(ChunkedFileDownload.class);

  private final long chunkSize;
  private final URI targetUrl;
  private final HttpClient client;
  private long offset = 0;

  public ChunkedFileDownload(long chunkSize, String targetUrl) throws URISyntaxException {
    this.chunkSize = chunkSize;
    this.targetUrl = new URI(targetUrl);
    this.client = HttpClient.newBuilder()
      .build();
  }

  public int contentLength() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(targetUrl)
      .setHeader("User-Agent", "ru.zubkoff.hw17.downloader.FragmentalFileDownload")
      .HEAD()
      .build();
    HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

    if(response.statusCode() != 200) {
      throw new RuntimeException("Status code must be 200 on contentLength but was " + response.statusCode());
    }
    return response.headers().firstValue("Content-Length").map(Integer::parseInt).orElseThrow();
  }

  public byte[] nextChunk() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(targetUrl)
      .setHeader("User-Agent", "ru.zubkoff.hw17.downloader.FragmentalFileDownload")
      .setHeader("range", "bytes=" + offset + "-" + (offset + chunkSize))
      .GET()
      .build();
    HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

    if(response.statusCode() != 206) {
      throw new RuntimeException("Status code must be 206 on nextChunk but was " + response.statusCode());
    }
    log.info("chunk " + (offset / chunkSize) + " obtained");
    offset += response.body().length;
    return response.body();
  }

  public long getChunkSize() {
    return chunkSize;
  }

}
