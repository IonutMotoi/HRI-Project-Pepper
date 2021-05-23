import keyboard
from pywinauto import Desktop
from pywinauto.application import Application

###WRITE TEXT
#keyboard.write(text)

###GET ALL OPENED WINDOWS NAME

# windows = Desktop(backend="uia").windows()
# print([w.window_text() for w in windows])

#app = Application(backend='uia').start("C:/Program Files/Android/Android Studio/bin/studio64.exe").connect(title='My Application – robotsdk.xml [My_Application.app]')
#app = Application(backend='win32').connect(title='Robot Viewer')
app = Application().connect(title='Robot Viewer')
more = app.RobotViewer
more.wrapper_object()
more.set_focus()
#app.RobotViewer.menu_select('Display 3d information')
# dlg_spec = app.RobotViewerapp.FreeAgent.menu_select("File->Import and Export->Export Address Book")
# actionable_dlg = dlg_spec.wait('visible')


