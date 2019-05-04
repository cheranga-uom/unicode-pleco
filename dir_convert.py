import requests
import json
import os

apikey="abcd1234"

files = [f for f in os.listdir('.') if os.path.isfile(f)]
try:
	os.mkdir("converted")
except FileExistsError as e:
	print("Folder already there")
for fname in files:
    print(fname)
    with open(fname, 'rb') as f:
        r = requests.post('http://localhost:8080/api/upload', headers={"apikey":apikey}, files={'file': f},data={"inputfiletype":"officeXML"})
        print(r)
        rj = json.loads(r.content)
        if(rj["status"]=="success"):
            print('Beginning file download with requests')
            url = "http://localhost:8080/api/download?conversionId=" + str(rj["conversionId"])
            download_response = requests.get(url, headers={"apikey":apikey} )
            # dr_headers = json.loads(download_response.headers)
            conv_file_name=('./converted/converted-'+fname)
            with open(conv_file_name, 'wb') as f:  
                f.write(download_response.content)