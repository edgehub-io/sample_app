import numpy as np
import argparse
import controller
import imutils
import cv2
import time
import requests
import webbrowser
import sys

url = 'http://34.217.40.191:8080/GroveSimpleServlet/rotaryvalue'
threshold = 5152000
maxVal = 0

def reset():
	try:
		print "Got new criminal request!"
		r = requests.post(url, data=str("reset"))
		print "Reset completed!"

def match(imagePath, template):
	try:
		controller.turn_buzzer_off()
		template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
		template = cv2.Canny(template, 50, 200)
		(tH, tW) = template.shape[:2]
		# load the image, convert it to grayscale, and initialize the
		# bookkeeping variable to keep track of the matched region
		image = cv2.imread(imagePath)
		gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
		found = None
		
		print "Start matching..."
		# loop over the scales of the image
		for scale in np.linspace(0.2, 1.0, 20)[::-1]:
			# resize the image according to the scale, and keep track
			# of the ratio of the resizing
			resized = imutils.resize(gray, width = int(gray.shape[1] * scale))
			r = gray.shape[1] / float(resized.shape[1])

			# if the resized image is smaller than the template, then break
			# from the loop
			if resized.shape[0] < tH or resized.shape[1] < tW:
				break

			# detect edges in the resized, grayscale image and apply template
			# matching to find the template in the image
			edged = cv2.Canny(resized, 50, 200)
			result = cv2.matchTemplate(edged, template, cv2.TM_CCOEFF)
			(_, maxVal, _, maxLoc) = cv2.minMaxLoc(result)

			# if we have found a new maximum correlation value, then ipdate
			# the bookkeeping variable
			if found is None or maxVal > found[0]:
				found = (maxVal, maxLoc, r)

		print "maxVal_" + str(imagePath).split('/')[1] + ":" + str(maxVal)
		if (maxVal >= threshold):
			print "GOOD"
			r = requests.post(url, data=str("camera1"))
			print r.text
			if ("CRIMINAL FOUND" in r.text):
				print "*************************************"
				print "CRIMINAL LOCATED!"
				print "*************************************"
				controller.turn_buzzer_on()
			# unpack the bookkeeping varaible and compute the (x, y) coordinates
			# of the bounding box based on the resized ratio
			(_, maxLoc, r) = found
			(startX, startY) = (int(maxLoc[0] * r), int(maxLoc[1] * r))
			(endX, endY) = (int((maxLoc[0] + tW) * r), int((maxLoc[1] + tH) * r))

			# draw a bounding box around the detected result and display the image
			cv2.rectangle(image, (startX, startY), (endX, endY), (0, 0, 255), 2)
			if (maxVal >= threshold):
				cv2.imwrite('result_' + str(imagePath).split('/')[1], image)
			webbrowser.open('result_' + str(imagePath).split('/')[1])
		else:
			print "BAD"
		print "========================\n"
