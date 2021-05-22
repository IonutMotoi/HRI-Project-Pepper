import speech_recognition as sr
import keyboard
from pywinauto import Desktop
from pywinauto.application import Application

r = sr.Recognizer()

with sr.Microphone(device_index=1) as source:
    #r.adjust_for_ambient_noise(source, duration=1)
    #r.energy_threshold = 300
    print("Speak Anything :")
    audio = r.listen(source)
    
    try:
        text = r.recognize_google(audio, language="en-US")
        print("You said (US) : {}".format(text))
    except:
        print("Sorry could not recognize what you said")

###WRITE TEXT
#keyboard.write(text)

windows = Desktop(backend="uia").windows()
print(windows)