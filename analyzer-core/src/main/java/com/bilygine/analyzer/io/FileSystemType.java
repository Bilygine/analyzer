package com.bilygine.analyzer.io;

public enum FileSystemType {

  LOCAL(LocalFileSystem.class),
  GOOGLE_STORAGE(GSFileSystem.class);

  private Class<? extends FileSystem> clazz;

  FileSystemType(Class<? extends FileSystem> clazz) {
    this.clazz = clazz;
  }

  public Class<? extends FileSystem> getClazz() {
    return this.clazz;
  }
}