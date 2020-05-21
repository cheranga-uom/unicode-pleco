**Instructions a re focused on ubuntu. May slightly vary in RHEL versions.**

1. Install Nginx -  https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-18-04
2. Install Certbot (for ssl) - https://certbot.eff.org/lets-encrypt/ubuntubionic-nginx
3. Add the following special nginx configs to /etc/nginx/nginx.conf . You need to create another conf file for the pleco in sites-available and create a symlink to sites-enabled.
### special nginx configs

``http{
     #in seconds
    fastcgi_read_timeout 600;
    client_header_timeout 600;
    client_body_timeout 600;
 }``
 
 To extend the replying server time
 ``http{
  #Time to wait for the replying server
   proxy_read_timeout 600s;

}``

4.Download and install oracle jdk - https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
``sudo update-alternatives --config java``
5.Install maven
6. install mysql-server, create a database and a user. Then configure credentials in the ``unicode-pleco/srs/main/resources/application.yml``file. 
``spring:
    datasource:
        url: jdbc:mysql://localhost:3306/unicodepleco?useSSL=false
        username: root
        password: 1234``
7. Update the folder paths in application.yml, specially the root path. You can leave others to be as it is. *Set the absolute paths for the documents directory and logs*
``storage:
    Document:
        root: /home/gayan/Desktop/CiperLabs/unicode-pleco/Documents/``
8. Go to facebook developer and create a client credentials, and update the credes in yml file. You can use the existing ones for the pleco.uom.lk domain.
9. install libreoffice ( dependency for jodconverter - used for doc to docx conversion) - ``sudo apt install libreoffice-common``
10. Following script is for auto starting after a system shutdown using a service.

# 



place the script at /etc/systemd/system/pleco.service
``
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
``
### Create a user to run the systemd service and grant privilleges to execute the jar
``sudo useradd plecoweb -s /sbin/nologin -M
chmod +x target/unicode-pleco-0.0.1-SNAPSHOT.jar``

``sudo systemctl daemon-reload``

# Enable at start up of server
``sudo systemctl enable pleco
sudo systemctl start pleco
sudo journalctl -f -n 1000 -u pleco``


# You need following project installed as dotnetcore project for pdf conversion
https://github.com/GayanSandaruwan/PDFConverter
