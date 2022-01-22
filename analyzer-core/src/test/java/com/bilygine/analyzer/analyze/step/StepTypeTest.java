package com.bilygine.analyzer.analyze.step;

import com.bilygine.analyzer.analyze.steps.Step;
import com.bilygine.analyzer.analyze.steps.StepType;
import com.bilygine.analyzer.base.LocalModeUnitCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class StepTypeTest {

	@Test
	public void getImplementationTest() {
		for (StepType type : StepType.values()) {
			// Unknown implem. for this type return null
			Assert.assertNull(type.getImplementation(RandomStringUtils.randomAlphabetic(10)));
			for (Class implem : type.getImplementations()) {
				Assert.assertNotNull(type.getImplementation(implem.getName()));
			}
		}
	}

}
