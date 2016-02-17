pass='password'


# Creates a CA for nurses
# Common name should be: nurse
echo "creating self-signed cert"
openssl req -x509 -newkey rsa:2048 -keyout CA_Nurse.key -passout pass:$pass -out CA_Nurse.crt -days 90 -subj "/CN=Nurse"
