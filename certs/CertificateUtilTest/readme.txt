1. generate the server certificate by cer_generator(https://github.com/gatement/cert_generator)

2. transfor .p12 to .keystore:
keytool -importkeystore -srckeystore keycert.p12 -destkeystore key.keystore -srcstorepass 123456 -deststorepass 123456

3. list keystore entries:
keytool -list -keystore key.keystore -storepass 123456
