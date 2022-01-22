package com.bilygine.analyzer.io;

public abstract class FileSystem {

	abstract void write(String path, byte[] b) throws Exception;

	void write(String path, String s) throws Exception {
		write(path, s.getBytes());
	}

	abstract String read(String path) throws Exception;

	abstract boolean exists(String path) throws Exception;

	abstract boolean delete(String path) throws Exception;

	abstract void mkdir(String path) throws Exception;

	abstract void mkdirs(String path) throws Exception;
}
