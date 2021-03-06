#!/usr/bin/env python3

from vosk import Model, KaldiRecognizer, SetLogLevel
import sys
import os
import wave
import json

SetLogLevel(-1)

if not os.path.exists("model"):
    print ("Please download the model from https://alphacephei.com/vosk/models and unpack as 'model' in the current folder.")
    exit (1)

wf = wave.open(sys.argv[1], "rb")
if wf.getnchannels() != 1 or wf.getsampwidth() != 2 or wf.getcomptype() != "NONE":
    print ("Audio file must be WAV format mono PCM.")
    exit (1)

model = Model("model")
rec = KaldiRecognizer(model, wf.getframerate())

res = []

while True:
    data = wf.readframes(2000)
    if rec.AcceptWaveform(data):
        result = json.loads(rec.FinalResult())
        res.append(result["text"])
    if len(data) == 0:
        break

result = json.loads(rec.FinalResult())
res.append(result["text"])

print(json.dumps(res))
