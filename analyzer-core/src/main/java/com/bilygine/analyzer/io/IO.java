package com.bilygine.analyzer.io;

import com.bilygine.analyzer.configuration.Config;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

/**
 * IO Service multi filesystem
 */
public class IO {

  private static final Logger LOGGER = LogManager.getLogger(IO.class);

  private static FileSystem INSTANCE = null;

  public static void init() {
    FileSystemType type = null;
    String typeName = Config.FS_TYPE.stringValue().toUpperCase();
    if (EnumUtils.isValidEnum(FileSystemType.class, typeName)) {
      type = FileSystemType.valueOf(typeName);
    }
    try {
      INSTANCE = FileSystemFactory.create(type);
    } catch (InstantiationException | IllegalAccessException e) {
      LOGGER.error(e);
    }
  }

  public static void write(String path, String s) throws Exception {
    INSTANCE.write(path, s);
  }

  public static String read(String path) throws Exception {
    return INSTANCE.read(path);
  }

  public static boolean exists(String path) throws Exception {
    return INSTANCE.exists(path);
  }

  public static boolean delete(String path) throws Exception {
    return INSTANCE.delete(path);
  }

  public static void mkdir(String path) throws Exception {
    INSTANCE.mkdir(path);
  }

  public static void mkdirs(String path) throws Exception {
    INSTANCE.mkdirs(path);
  }
}
