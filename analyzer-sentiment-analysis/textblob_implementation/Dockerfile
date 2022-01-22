FROM python:3.7-stretch


RUN mkdir credentials
ENV FLASK_ENV=development
ENV FLASK_APP=sentiment_textblob.py

COPY . ./code
WORKDIR ./code
RUN pip install -r requirements.txt

EXPOSE 5003

ENTRYPOINT ["flask","run", "-h","0.0.0.0", "-p", "5003"]

