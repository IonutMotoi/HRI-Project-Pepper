   
import speech_recognition as sr

r = sr.Recognizer()
with sr.Microphone() as source:
    #r.adjust_for_ambient_noise(source, duration=1)
    r.energy_threshold = 300
    print("Speak Anything :")
    audio = r.listen(source)
    
    try:
        text = r.recognize_google(audio, language="en-US")
        print("You said (US) : {}".format(text))
    except:
        print("Sorry could not recognize what you said")

    try:
        text = r.recognize_google(audio, language="en-GB")
        print("You said (GB) : {}".format(text))
    except:
        print("Sorry could not recognize what you said")