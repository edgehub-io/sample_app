import socket
import webbrowser
import matcher
import sys
from time import sleep

TCP_IP = '34.217.40.191'
TCP_PORT = 8091
BUFFER_SIZE = 1024

def checkupdate():
	i = 0
	while True:
		try:
			will_update = False
			s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
			s.connect((TCP_IP, TCP_PORT))
			s.send(str(i))
			data = s.recv(BUFFER_SIZE)
			print len(data)
			if (len(data) > 2):
				with open('badguy.jpg', 'wb') as img:
					while True:
						img.write(data)
						data = s.recv(BUFFER_SIZE)
						will_update = True
						if not data:
							break	
					
					if will_update:
						print "updating criminal:", i
						i = i + 1
						matcher.reset()
					img.close()
					print "criminal updated!"
			s.close()
			sleep(5)
