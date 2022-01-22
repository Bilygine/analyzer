package com.bilygine.analyzer.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileSystemFactory {

	private static final Logger LOGGER = LogManager.getLogger(FileSystemFactory.class);
	private static final FileSystemType DEFAULT_FS_TYPE = FileSystemType.LOCAL;

  public static FileSystem create(FileSystemType type) throws IllegalAccessException, InstantiationException {
  	Class<? extends FileSystem> clazz;
  	if (type == null || type.getClazz() == null) {
  		clazz = DEFAULT_FS_TYPE.getClazz();
  		LOGGER.warn("No filesystem found in configuration (Default=%s)", DEFAULT_FS_TYPE.name());
		} else {
			clazz = type.getClazz();
		}
		return clazz.newInstance();
	}
}
