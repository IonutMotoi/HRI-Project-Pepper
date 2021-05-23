import speech_recognition as sr
import keyboard
from pywinauto import Desktop
from pywinauto.application import Application
import time 

# Initialize speech recognition
r = sr.Recognizer()

# Initialize "Dialog view" window controller
app = Application().connect(title='Dialog view')
more = app.DialogView
more.wrapper_object()

robot_is_speaking = False

while(True):
    if not robot_is_speaking:
        with sr.Microphone(device_index=1) as source:
            r.adjust_for_ambient_noise(source)
            #r.energy_threshold = 300
            print("Speak Anything :")
            audio = r.listen(source)
            
            try:
                text = r.recognize_google(audio, language="en-US")
                print("You said (US) : {}".format(text))
                
                more.set_focus()
                time.sleep(0.1)
                keyboard.write(text)
                keyboard.press('enter')

                robot_is_speaking = True
            except:
                print("Sorry could not recognize what you said")
    else:
        time.sleep(3)
        robot_is_speaking = False