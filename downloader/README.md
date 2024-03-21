### Школа Java Developer
#### Домашнее задание №17

Тестовое задание: Есть список ссылок в текстовом файле. Нужно написать программу, которая при старте будет скачивать эти файлы и складывать в указанную папку на локальном диске. Программа должна уметь качать несколько файлов одновременно (в несколько потоков, например, 3 потока) и выдерживать указанное ограничение на скорость загрузки, например, 500 килобайт в секунду. Программу можно сделать консольной, можно использовать spring-boot. Для работы с http, с ограничением скорости и любыми манипуляциями с данными можно брать любую библиотеку. При желании, можно любую часть сделать самостоятельно. Нужно учитывать, что программа может развиваться, из нее могут быть переиспользованы полезные части. Поэтому крайне желательно использовать абстракции (ООП, интерфейсы, паттерны, SOLID и т.д.).


```java
public class ChunkedFileDownload {
  public ChunkedFileDownload(long chunkSize, String targetUrl) throws URISyntaxException {
    ...
  }

  ...
}
```
Представляет собой загрузку одного файла range запросами размером по chunkSize, загрузка реализована в методе nextChunk()

```java
public interface Downloader {
  byte[] download(String targetUrl) throws URISyntaxException, IOException, InterruptedException;
}
```
Интерфейс загрузчика файлов

```java
public interface Limiter<T> {
  T performLimited(Supplier<T> action) throws InterruptedException;
}
```
Лимитор загрузчика выполняет action согласно условиям, по заданию нужно было сделать ограничитель скорости загрузки, но это не единственный лимитор который можно сделать, напрмиер глобальный ограничитель реквестов тоже можно реализовать через этот интерфейс

```java

public class ChunkDownloadSpeedLimiter implements Limiter<byte[]> {
  public ChunkDownloadSpeedLimiter(long speedLimit) {
    ...
  }
    ...
}
```
Ограничитель скорости загрузки, смотрит сколько байтов вернул action и за сколько, если скорость превышена назначаем штраф ожиданием равный времени за которое превышение speedLimit будет скачано за speedLimit

```java
public class SpeedLimitedChunkedFileDownloader implements Downloader {
  public SpeedLimitedChunkedFileDownloader(ChunkDownloadSpeedLimiter limiter) {
    ...
  }
  ...
}
```
Загрузчик файлов с ChunkDownloadSpeedLimiter

```java
public class DownloadManager {
  public DownloadManager(ExecutorService executor, Path destination, Downloader downloader) {
    ...
  }

  public void downloadAll(String... sourcesUrls) {
    ...
  }

  public void waitForAllDownloadsComplete() {
    ...
  }

```
Выполняет скачивание файлов с sourcesUrls и сохранение их в destination с помощью downloader'a потоками executor'а, методом waitForAllDownloadsComplete() можем заблокировать вызывающий поток до момента скачивания всех файлов, ждать будем только задачи добавленные вызываемым потоком

#### Как запустить?
Программе нужно передать следующие аргументы в консоли

--download.speed=<байт в секунду>, опциональный параметр если не указать возьмется дефолтное значение из application.yml
 
--thread.count=<число потоков>, опциональный параметр если не указать создастся пул виртуальных потоков

--sources=<файл с ссылками на скачиваемые ресурсы>, обязательный параметр

--destination=<путь куда буду сохранены файлы>, обязательный параметр

Я оставил в методе main запуск с параметрами просто для удобства проверки
