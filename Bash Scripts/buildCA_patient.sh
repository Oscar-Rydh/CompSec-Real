pass='password'


# Creates a CA for patients 
# Common name should be: patient
echo "creating self-signed cert"
openssl req -x509 -newkey rsa:2048 -keyout CA_Patient.key -passout pass:$pass -out CA_Patient.crt -days 90 -subj "/CN=Patient"
