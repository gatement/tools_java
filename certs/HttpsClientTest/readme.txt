1. generate the server certificate and client certificate by cer_generator(https://github.com/gatement/cert_generator)

2. transform client .p12 to .keystore:
keytool -importkeystore -srckeystore ../all_certs/johnson/keycert.p12 -destkeystore client_keycert.keystore -srcstorepass 123456 -deststorepass 123456
keytool -list -keystore client_keycert.keystore -storepass 123456

3. transform server cacert.pem to .keystore:
keytool -importcert -noprompt -trustcacerts -file ../all_certs/ServerRootCA/cacert.pem -keystore server_cacert.keystore -storepass 123456
keytool -list -keystore server_cacert.keystore -storepass 123456
