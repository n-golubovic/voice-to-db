# voice-to-db
Voice-to-db is an application for speech-to-text conversion based on Vosk. It allows non-real-time conversion by request
submitting and later retrieval.
### How to start
This service can be started locally or on bare metal, but as it relies on both Python and Java, it is preferred to use
Docker. You can start the Docker instance using following commands:
```
docker build -t voice2db .
docker run voice2db -p8000:8080
```
This will create an instance of service that will listen to requests on port 3000. Change it if you want to. If you 
decide to run it locally, make sure you have Python 3.8.x installed. Depending on the platform, you might need to change
`voice-to-db.python-executable` to match the command which refers to python executable.

If you decide to run the application in Docker mode in live environment, consider mapping directories `/upload` and
`/results` to volumes. This will retain requests that are received but not processed in case you decide to update the
service logic.

### How to use
Two endpoints are available - `/upload` and `/result`. 

To submit a request, call `/upload` endpoint and provide audio
files through `files` field in form data. Calling this endpoint will result either in status `400` in case you fail to 
provide audio files, status `415` if the files you provided are audio files, but incompatible, or a response object
containing `requestId` field. You will later use this id to retrieve results. Note that voice-to-text is a very 
demanding operation, so it's hard to provide real-time results. This is why you will poll for results some time after
submit.

To retrieve the results of a submitted job, call `/result?requestId={requestId}`, where `{requestId}` is the id received
after a successful job submit. Calling this endpoint will result either in status `404` if the service hasn't processed
the request yet (or if the request matching given requestId doesn't exist) or in a returned object containing `text`
field with the text representation of submitted audio files.

### How it works
Voice-to-db relies on queuing submitted requests and storing the files locally. FFMPEG converts received files into 
Vosk-compatible format, which are then stored. Vosk processes the files by the order they were submitted, which are 
then removed. Results remain as long as someone doesn't delete them. Since Vosk is in its experimental stage, Java 
support is flaky, so Vosk is called as a Python script from within Java.
