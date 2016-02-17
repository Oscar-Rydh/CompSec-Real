pass='password'


# Creates a CA for patients
# Common name should be: government
echo "creating self-signed cert"
openssl req -x509 -newkey rsa:2048 -keyout CA_Government.key -passout pass:$pass -out CA_Government.crt -days 90 -subj "/CN=Government"
