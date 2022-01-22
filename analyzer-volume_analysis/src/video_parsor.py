import os
import wave
import contextlib
import subprocess
from sample_audio import *


# POST route
def post_sound_url(func):
    def wrapper(args):
        self = args[0]
        self.app.add_url_rule(rule="/volume", )

# Convert video file in audio wave and delete files after analyse
class VideoParser:
    def __init__(self, file_path):
        self.sound_file_dir = file_path
        self.wave_audio = os.path.abspath("..") + "/audios/new_analyze.wav"

    # Convert mp3 video into audio wav
    def convert_to_wav(self):
        subprocess.call(['ffmpeg', '-i', '%s'% self.sound_file_dir, '%s' %self.wave_audio])

    # Return sound directory and total frames
    def get_sound_file_dir(self):
        self.convert_to_wav()
        with contextlib.closing(wave.open(self.wave_audio, 'r')) as f:
            frames = f.getnframes()
        return (self.wave_audio, frames)

    # Delete google file and sound file
    def delete_files(self):
        subprocess.run(["rm", "%s"%self.sound_file_dir])
        subprocess.run(["rm", "%s"%self.wave_audio])
