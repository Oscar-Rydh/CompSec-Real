

serverCommonName='MyServer'
pass='password'
CN="123123"

#1
echo "creating self-signed cert CA"
openssl req -x509 -newkey rsa:2048 -keyout CA.key -out CA.crt -days 90

#2
echo "creating empty truststore CLIENT"
keytool -genkey -alias cts -keystore clienttruststore

echo "import CA-cert to the clienttruststore CLIENT"
keytool -import -trustcacerts -alias root -file CA.crt -keystore clienttruststore

#3
echo "create clientkeystore with key pair CLIENT"
keytool -genkey -alias cks -keyalg RSA -keystore clientkeystore -keysize 2048


#4
echo "create crs for the key pair created in (3) CLIENT ENTER DIVISION AND STUFF!! "
keytool -certreq -alias cks -keystore clientkeystore -file client.csr

#5
echo "create certificate from client.crs CA SIGNING client certificate 
openssl x509 -req -days 360 -in client.csr -CA CA.crt -CAkey CA.key -CAcreateserial -out client.crt

#6
echo "import CA.crt in clientkeystore CLIENT"
keytool -import -trustcacerts -alias root -file CA.crt -keystore clientkeystore

echo "import the signed certificate client.crt to clientkeystore CLIENT"
keytool -import -trustcacerts -alias cks -file client.crt -keystore clientkeystore

echo "SWITCHING TO SERVER SIDE!!!!!!!!!!"

echo "do #(3-7)  to create serverkeystore, CN=MyServer"

echo "create clientkeystore with key pair SERVER"
keytool -genkey -alias sks -keyalg RSA -keystore serverkeystore -keysize 2048

echo "create crs for the key pair created above SERVER"
keytool -certreq -alias sks -keystore serverkeystore -file server.csr


openssl x509 -req -days 360 -in server.csr -CA CA.crt -CAkey CA.key -CAserial CA.srl -out server.crt

echo "IMPORT CA certificate to serverkeystore SERVER"
keytool -import -trustcacerts -alias root -file CA.crt -keystore serverkeystore

echo "IMPORT server certificate to serverkeystore SERVER"
keytool -import -trustcacerts -alias sks -file server.crt -keystore serverkeystore

keytool -list -v -keystore serverkeystore

#10
echo "create empty servertruststore"
keytool -genkey -alias sts -keystore servertruststore

echo "import CA-cert to servertruststore"
keytool -import -trustcacerts -alias root -file CA.crt -keystore servertruststore

