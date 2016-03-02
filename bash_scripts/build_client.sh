
CN="Regering"
TS="RegeringTrustStore"

#2


#echo "creating empty clienttruststore CLIENT"
#keytool -genkey -alias cts -keystore $TS

#echo "import CA-cert to the clienttruststore CLIENT"
#keytool -import -trustcacerts -alias root -file CA.crt -keystore $TS



printf " \n\n\n\n\n\n  This scripts helps you create a client. "

function pause(){
 read -n1 -rsp $'   Press any key to continue or Ctrl+C to exit...\n\n\n'
}

pause


#3
#echo "create clientkeystore with key pair CLIENT"
printf "\n\n ----- create clientkeystore with key pair CLIENT ------ \n\n-----  NOW IT'S TIME TO ENTER THE CLIENT DATA  ----- \n  CN = PERSONAL NUMBER\n OU = DIVISION\n O = USER\n\n\n"

keytool -genkey -alias cks -keyalg RSA -keystore $CN -keysize 2048

#4
echo "create crs for the key pair created in (3)"
keytool -certreq -alias cks -keystore $CN -file $CN.csr

#
echo "create certificate from client.csr CA SIGNING client certificate"
openssl x509 -req -days 360 -in $CN.csr -CA CA.crt -CAkey CA.key -CAserial CA.srl -out $CN.crt

#6
echo "import CA.crt in clientkeystore CLIENT"
keytool -import -trustcacerts -alias root -file CA.crt -keystore $CN 

echo "import the signed certificate client.crt to clientkeystore CLIENT"
keytool -import -trustcacerts -alias cks -file $CN.crt -keystore $CN
