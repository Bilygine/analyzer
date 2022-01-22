package com.bilygine.analyzer.analyze.steps.transcription;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.DefaultAnalyze;
import org.apache.commons.lang3.RandomUtils;


public class FakeTranscriptionStep extends TranscriptionStep {

	private static final String[] words = {"Hello", "Ale", "Wonderful", "Amazing", "Bilquis", "Soutenance", "Bastien", "Nicolas", "Esteban", "ETNA", "Bilygine", "Soutenance"};
	private Analyze owner;

	public FakeTranscriptionStep(DefaultAnalyze analyze) {
		super(analyze, FakeTranscriptionStep.class.getName());
		this.owner = analyze;
	}

	@Override
	public String getDescription() {
		return "This step create random couple of timestamp/word.";
	}

	@Override
	public void transcript() {
		// TODO: A fake transcripting can take same timestamp
		try {
			// Simulate transcription timing
			Thread.sleep(RandomUtils.nextInt(1000, 3000));
			int wordCount = RandomUtils.nextInt(10, 100);
			for (int i = 0; i < wordCount; i++) {
				long timestamp = RandomUtils.nextInt(1, 59); // Seconds between 1s - 1min
				int index = RandomUtils.nextInt(0, words.length);
				owner.getResultBuilder().registerWord((int) timestamp, words[index], timestamp, timestamp);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
