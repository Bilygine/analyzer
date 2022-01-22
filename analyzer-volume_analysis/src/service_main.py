from google_storage_client import *
from video_parsor import *
from sample_audio import *
from flask import Flask, request



app = Flask(__name__)

@app.route("/volume", methods=['POST'])
def main():
    if request.method == 'POST':
        long_url = request.args.get('URL', None)  # Return none if url not set
        # URL = "https://console.cloud.google.com/storage/browser/bilygine-audio/fr/video.mp3" OR
        # URL = "gs://bilygine-audio/fr/video.mp3"
        if long_url:
            url_var = long_url.split('/')[-2:]
            google_url = url_var[0] + "/" + url_var[1]

            # Download file
            gs_client = GSClient(google_url)
            storage_file = gs_client.get_data_from_url()

            # Convert Video into sound file
            video_parsor = VideoParser(storage_file)
            sound_file, total_frames = video_parsor.get_sound_file_dir()

            # Sample sound ang calculate volume norm for each second
            sound_sampler = SampleSoundFile(soundfile_name=sound_file)
            sound_sampler.sample_sound(total_frames)

            # Delete google storage video and audio
            video_parsor.delete_files()

            # Return results as json dumps
            return sound_sampler.set_up_sound_analyse_json_file()

app.run(port=8080, debug=True)