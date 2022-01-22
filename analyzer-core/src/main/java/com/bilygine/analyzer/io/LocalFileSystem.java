package com.bilygine.analyzer.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LocalFileSystem extends FileSystem {

	@Override
	public void write(String path, byte[] bytes) throws IOException {
		try (OutputStream os = new FileOutputStream(path)) {
			os.write(bytes);
		}
	}

	@Override
	public String read(String path) throws IOException {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines( Paths.get(path), StandardCharsets.UTF_8))
		{
			stream.forEach(s -> contentBuilder.append(s));
		}
		return contentBuilder.toString();
	}

	@Override
	boolean exists(String path) {
		return new File(path).exists();
	}

	@Override
	boolean delete(String path) {
		return new File(path).delete();
	}

	@Override
	void mkdir(String path) {
		new File(path).mkdir();
	}

	@Override
	void mkdirs(String path) {
		new File(path).mkdirs();
	}
}
