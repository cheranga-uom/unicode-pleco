place the script at /etc/systemd/system/pleco.service

[Unit]
Description=Unicode Pleco webapp
After=syslog.target

[Service]
ExecStart=/home/plecoadm/unicode-pleco/target/unicode-pleco-0.0.1-SNAPSHOT.jar
User=plecoweb
Restart=on-failure
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target




#>sudo useradd plecoweb -s /sbin/nologin -M

#>chmod +x target/unicode-pleco-0.0.1-SNAPSHOT.jar

#>sudo systemctl daemon-reload

# Enable at start up of server
#>systemctl enable pleco
#>systemctl start pleco


