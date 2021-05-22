   
import speech_recognition as sr

r = sr.Recognizer()
print(sr.Microphone.list_microphone_names())
mic = sr.Microphone(device_index=1)
with mic as source: 
   r.adjust_for_ambient_noise(source) 
   audio = r.listen(source) 
result = r.recognize_google(audio)
print(result)