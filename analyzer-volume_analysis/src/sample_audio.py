import soundfile as sf
import numpy as np
import json


#This class sample an audio file per second and save volume level per second
class SampleSoundFile:

    def __init__(self, soundfile_name):
        self.soundfile_name = soundfile_name
        self.sample_value = 44100 # Sample data per 1s
        self.timestamp_and_volume = {}
        self.data_id = 0
        self.my_json = None

    def set_up_sound_analyse_json_file(self):
        return json.dumps([{"timestamp":k, "volume_norme": v} for k,v in self.timestamp_and_volume.items()], indent=4)

    # Calculate Norm of sound data matrix
    def set_volume_per_frame(self, sound_data_array):
        for data in sound_data_array:
            volume_norm = np.linalg.norm(data) * 10
            self.data_id += 1
            self.timestamp_and_volume[self.data_id] = int(volume_norm)

    # Read audio file per second and
    def sample_sound(self, frames):
        sound_data = []
        frame_to_read = 44100
        read_from = 0
        while frames != 0:
            if frames > frame_to_read:
                read_frames = frame_to_read
            else:
                read_frames = frames
            data, _ = sf.read(self.soundfile_name, frames=read_frames, start=read_from, dtype='float64')
            read_from += read_frames + 1
            sound_data.append(data)
            frames -= read_frames
        self.set_volume_per_frame(sound_data)
