package memetalk.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import memetalk.ConfigReader;

/** StaticFileManager manages static files located under `resources` folder. */
public class StaticFileManager {

  /** Writes a file to storage and returns an URL reference. */
  public String write(ConfigReader configReader, String filename, byte[] content) {
    try {
      String dirPath = configReader.getConfig("static-file-dir");
      createDirIfNotExists(dirPath);
      Path p = Paths.get(dirPath + filename);
      Files.write(p, content);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return configReader.getConfig("static-file-url-prefix") + filename;
  }

  private void createDirIfNotExists(String dirPath) {
    File dir = new File(dirPath);
    if (!dir.exists()) {
      dir.mkdir();
    }
  }
}
