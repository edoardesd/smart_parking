import paho.mqtt.client as mqtt
import time
import base64


def on_publish(mosq, userdata, mid):
    print("message sent")

client = mqtt.Client()
client.connect("broker.hivemq.com", 1883, 60)
client.on_publish = on_publish

while True:
    # f=open("test.png", "rb") #3.7kiB in same folder
    # fileContent = f.read()
    # byteArr = bytearray(fileContent)

    with open("test.png", "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read())
    
        client.publish("smart/parking/polimi", encoded_string, qos=2, retain=True)

    print("published")
    time.sleep(20)

client.loop_forever()