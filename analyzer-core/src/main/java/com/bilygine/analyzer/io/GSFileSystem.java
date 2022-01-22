package com.bilygine.analyzer.io;

import com.bilygine.analyzer.entity.error.AnalyzerError;

public class GSFileSystem extends FileSystem {
	@Override
	public void write(String path, byte[] b) throws Exception{
	  throw new AnalyzerError("GSFileSystem write() is not implemented");
	}

	@Override
	public String read(String path) throws Exception {
	  throw new AnalyzerError("GSFileSystem read() is not implemented");
	}

	@Override
	boolean exists(String path) throws Exception {
		throw new AnalyzerError("GSFileSystem exists() is not implemented");
	}

	@Override
	boolean delete(String path) throws Exception {
		throw new AnalyzerError("GSFileSystem delete() is not implemented");
	}

	@Override
	void mkdir(String path) throws Exception {
		throw new AnalyzerError("GSFileSystem mkdir() is not implemented");
	}

	@Override
	void mkdirs(String path) throws Exception {
		throw new AnalyzerError("GSFileSystem mkdirs() is not implemented");
	}
}
