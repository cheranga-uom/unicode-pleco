import requests
import json
import os

apikey="abcd1234"
api_upload_path="https://pleco.uom.lk/api/upload"
api_download_path="https://pleco.uom.lk/api/download?conversionId="
files = [f for f in os.listdir('.') if os.path.isfile(f)]
logfile="log.txt"
try:
	os.mkdir("converted")
except FileExistsError as e:
	print("Folder already there")
with open(logfile, 'a') as log:
    for fname in files:
        print(fname)
        with open(fname, 'rb') as f:
            r = requests.post(api_upload_path, headers={"apikey":apikey}, files={'file': f},data={"inputfiletype":"officeXML"})
            rj = json.loads(r.content)
            print(rj)
            log.write(str(r.content) + "\n")
            if(rj["status"]=="success"):
                print('Downloading converted file'+fname)
                url = api_download_path + str(rj["conversionId"])
                download_response = requests.get(url, headers={"apikey":apikey} )
                # dr_headers = json.loads(download_response.headers)
                conv_file_name=('./converted/converted-'+fname)
                with open(conv_file_name, 'wb') as f:
                    f.write(download_response.content)
