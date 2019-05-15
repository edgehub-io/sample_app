# sample_app
Simple picture matching app using opencv2
===

## Server-side
GroveSimpleServlet
> receives POST from grove side, and keeps track of the count of badguy found. This servlet is running on an EC2 instance using tomcat.

SimpleImageReceiver
> receives new badguy picture from image sender. This server is running on the same EC2 instance, communicating with client's SimpleImageSender via TCP socket.

SimpleImageReturn
> receives messages from grove side to update badguy's picture to the grove side. This server is running on the same EC2 instance, communicating with grove side's client via TCP socket.

## Grove-side
Before running grove-side app, we need to first start edgex services:
> docker-compose -f docker-compose-demo-grove.yml up --detach

On grove side, we can either run it directly:

> python match.py

or run it using docker image (`anduinlothar/dockerhub:match_sample_app`)

> docker run -it --network="host" match_sample_app

It will try to match badguy.jpg with all pictures in (`images/`) directory

1. Each press of the grove button, one picture will be matched with the template. To match next picture, press again.
2. During matching process for each picture, the red led light will turn on, informing us the matching is in progress.
3. If 5 pictures match the badguy, buzzer will buzz. Press button to continue matching and buzzer buzzing will stop.
4. The grove-side app will check with SimpleImageReturn to see if badguy is updated. If yes, the new badguy.jpg will be downloaded and replace the old one.

## Client-side
SimpleImageSender
It is a simple jar that sends a picture of the badguy to SimpleImageReceiver.
To use it, simple run the jar and provide the picture file:
> java -jar simpleimagesender.jar warakawa_son.jpg

