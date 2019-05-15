# USAGE
# python match.py

# import the necessary packages
import argparse
import imutils
import matcher
import update_checker
import controller
import glob
import cv2
import time
import threading
import sys

update_thread = threading.Thread(target=update_checker.checkupdate)
update_thread.daemon = True
update_thread.start()

# load the image image, convert it to grayscale, and detect edges

imageFiles = glob.glob("images/*.jpg")
numPics = len(imageFiles)
print numPics

button_pressed = 1
button_released = 0
i = 0

while True:
	try:
		button_status = controller.check_button()
		if button_status == button_pressed:
			print "Got new photo!"
			controller.turn_light_on()						
			if (i >= numPics):
				i = 0
			imagePath = (imageFiles)[i]
			print "Matching photo " + imagePath
			template = cv2.imread('badguy.jpg')
			matcher.match(imagePath, template)
			i = i + 1
			button_status = button_released
		else:
			controller.turn_light_off()			
	except KeyboardInterrupt, SystemExit:
		controller.turn_light_off()
		controller.turn_buzzer_off()
		sys.exit() 
		break
	except (IOError,TypeError) as e:
		print("Error")
