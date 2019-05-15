import requests
import json

# grove communication with edgex
headers = {'Content-Type' : 'application/json'}

button_url = 'http://localhost:49991/api/v1/device/all/Get_ButtonState'

light_url = 'http://localhost:49991/api/v1/device/all/Set_RedLed'
light_on_data = json.dumps({'Red-LED':'true'})
light_off_data = json.dumps({'Red-LED':'false'})

buzzer_url = 'http://localhost:49991/api/v1/device/all/Set_Buzzer'
buzzer_on_data = json.dumps({'Grove-Buzzer':'true'})
buzzer_off_data = json.dumps({'Grove-Buzzer':'false'})


def check_button():
	response = requests.get(button_url, headers=headers)
	if (len(response.text.split(':')) > 1):
		return int(response.text.split(':')[1][1])
        return 0

def turn_light_on():
	response = requests.post(light_url, data=light_on_data, headers=headers)

def turn_light_off():
	response = requests.post(light_url, data=light_off_data, headers=headers)

def turn_buzzer_on():
	response = requests.post(buzzer_url, data=buzzer_on_data, headers=headers)

def turn_buzzer_off():
	response = requests.post(buzzer_url, data=buzzer_off_data, headers=headers)
