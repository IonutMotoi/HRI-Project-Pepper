import speech_recognition as sr
import keyboard
from pywinauto import Desktop
from pywinauto.application import Application
import time 

# Initialize speech recognition
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
# Initialize "Dialog view" window controller
app = Application().connect(title='Dialog view')
more = app.DialogView
more.wrapper_object()
more.set_focus()
time.sleep(1)
keyboard.write(text)
keyboard.press('enter')
