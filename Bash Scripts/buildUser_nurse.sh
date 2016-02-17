#CN (common name) = pnbr
#OU (organisation unit) = division
#O (organisation) = usertype

#password should be password as of now

CN="123123"
#lowercase letters
OU="division" 
O="nurse/doctor/..."

echo "create clientkeystore with key pair"
keytool -genkey -alias cks -keyalg RSA -keystore $CN -keysize 2048

echo "create crs for the key pair created in (3)"
keytool -certreq -alias cks -keystore $CN -file $CN.csr 

echo "create certificate from client.crs"
openssl x509 -req -days 360 -in $CN.csr -CA CA_Nurse.crt -CAkey CA_Nurse.key -CAcreateserial -out $CN.crt

echo "import CA.crt in clientkeystore"
keytool -import -trustcacerts -alias root -file CA_Nurse.crt -keystore $CN

echo "import the signed certificate client.crt to clientkeystore"
keytool -import -trustcacerts -alias cks -file $CN.crt -keystore $CN
