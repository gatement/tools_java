# 1. generate the server certificate by cer_generator(https://github.com/gatement/cert_generator)

# 2. transfor .p12 to .keystore:
rm -rf key.keystore
keytool -importkeystore -srckeystore ../all_certs/local.com/keycert.p12 -destkeystore key.keystore -srcstorepass 123456 -deststorepass 123456
keytool -list -keystore key.keystore -storepass 123456

# 3. copy certificates:
rm -rf cert.pem cert.cer
cp ../all_certs/local.com/cert.pem .
cp ../all_certs/local.com/cert.cer .
