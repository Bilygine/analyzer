from google.cloud import storage
import os

# TODO: download mp3 if it is raw which is send

# Create a google storage client to download a video
class GSClient:
    def __init__(self, url):
        self.storage_client = storage.Client("Bilygine")
        self.bucket_name = "bilygine-audio"
        self.url = url
        self.file_path = os.path.abspath("..") + "/audios/new_analyze.mp3"

    # Download and return file path
    def get_data_from_url(self):
        bucket = self.storage_client.get_bucket(self.bucket_name)
        blob = bucket.blob(self.url)
        blob.download_to_filename(self.file_path)
        return self.file_path

