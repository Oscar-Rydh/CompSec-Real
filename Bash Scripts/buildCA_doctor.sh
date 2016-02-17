pass='password'


# Creates a CA for doctors
# Common name should be: Doctor

echo "creating self-signed cert"
openssl req -x509 -newkey rsa:2048 -keyout CA_Doctor.key -passout pass:$pass -out CA_Doctor.crt -days 90 -subj "/CN=Doctor"

