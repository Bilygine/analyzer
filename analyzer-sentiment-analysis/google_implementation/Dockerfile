FROM python:3.6-stretch


RUN mkdir credentials
ENV FLASK_ENV=development
ENV FLASK_APP=sentiment_google.py
ENV GOOGLE_APPLICATION_CREDENTIALS=/credentials/bilygine_google_credentials.json

COPY . ./code
WORKDIR ./code
RUN pip install -r requirements.txt

EXPOSE 5002

ENTRYPOINT ["flask","run", "-h","0.0.0.0", "-p", "5002"]
