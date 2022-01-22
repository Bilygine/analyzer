package com.bilygine.analyzer.io;

import com.bilygine.analyzer.base.LocalModeUnitCase;
import com.bilygine.analyzer.configuration.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LocalFileSystemTest extends LocalModeUnitCase {

	@Before
	public void before() {
		Config.FS_TYPE.setValue("LOCAL");
		IO.init();
	}

	@Test
	public void testCreateReadFile() throws Exception {
		final String PATH = "myFile";
		final String EXPECTED = "my amazing content";
		IO.write(PATH, EXPECTED);
		Assert.assertEquals(EXPECTED, IO.read(PATH));
	}

	@Test
	public void testExistsFile() throws Exception {
		final String MY_FILE = "myFutureFile";
		Assert.assertFalse(IO.exists(MY_FILE));
		IO.write(MY_FILE, "");
		Assert.assertTrue(IO.exists(MY_FILE));
		IO.delete(MY_FILE);
		Assert.assertFalse(IO.exists(MY_FILE));
	}
}
