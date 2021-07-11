import requests
 
url = "https://bettercallpepper.altervista.org/api/callPepper.php"
bed_label = "3"
flag = "true"
 
#requests.post(url, json={'room': bed_label, 'button': flag})

payload = {'room': bed_label, 'button': flag}
r = requests.get(url, params=payload)

#flag = "false"
 
#requests.post(url, json={'room': bed_label, 'button': flag})

#payload = {'room': 1, 'button': flag}
#r = requests.get(url, params=payload)

#payload = {'room': 2, 'button': flag}
#r = requests.get(url, params=payload)


