echo "Creating serverkeystores, CN=MyServer"

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

