#!/bin/bash

# Command-line args:
# $1: text from which audio file will be synthesized
# $2: name of audio file (without .mp3 extension)
#
# e.g. ./gcloud-synthesize-audio-from-text.sh "Everyone, close your eyes and extend your hand into a fist in front of you." testwavenet

export GOOGLE_APPLICATION_CREDENTIALS="/Users/leewayleaf/Documents/Repositories/narradir-android/gcloud-service-account-file.json"

VOICE_LOCALE='en-us'
VOICE_NAME='en-US-Wavenet-B'
VOICE_GENDER='MALE'

TEXT=$1
AUDIOFILE_DIR="app/src/main/res/raw"
AUDIOFILE_NAME=$2
if [ -z "$AUDIOFILE_NAME" ]
  then AUDIOFILE_NAME="output"
fi

# Synthesize audio from text from command-line on Google Cloud

curl -H "Authorization: Bearer $(~/google-cloud-sdk/bin/gcloud auth application-default print-access-token)" \
  -H "Content-Type: application/json; charset=utf-8" \
  --data "{
    'input':{
      'text':'$TEXT'
    },
    'voice':{
      'languageCode':'$VOICE_LOCALE',
      'name':'$VOICE_NAME',
      'ssmlGender':'$VOICE_GENDER'
    },
    'audioConfig':{
      'audioEncoding':'MP3'
    }
  }" "https://texttospeech.googleapis.com/v1/text:synthesize" > gcloud-synthesized-text.txt

# Keep only the substring between `  "audioContent": "` and `"`. Redirect output back to the same text file from which input originally came

cat gcloud-synthesized-text.txt | grep "audioContent" | sed -e 's/  "audioContent": "\(.*\)"/\1/' | tee gcloud-synthesized-text.txt >/dev/null

# Decode base64 format of text file

base64 gcloud-synthesized-text.txt --decode > $AUDIOFILE_DIR/$AUDIOFILE_NAME.mp3
