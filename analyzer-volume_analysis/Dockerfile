# Build Volime Analyze with python3
FROM python:3-alpine

# Update and install usefull packets
RUN apk update && apk upgrade && pip install -U pip
RUN apk add --update alpine-sdk make gcc python3-dev python-dev libxslt-dev libxml2-dev libc-dev openssl-dev libffi-dev zlib-dev py-pip openssh \
    && rm -rf /var/cache/apk/*
RUN apk add libsndfile-dev
RUN apk add ffmpeg

# Create workdir
RUN mkdir -p /volume-analysis
WORKDIR /volume-analysis
RUN mkdir -p audios
RUN mkdir -p src

# Copy sources in
COPY src src/
COPY requirements.txt .

# Install requirements
RUN pip install -r requirements.txt
RUN echo ${PWD}
WORKDIR /volume-analysis/src
RUN echo ${PWD}

# Run service
CMD ["python3", "service_main.py"]
