# 1. generate the server certificate by cer_generator(https://github.com/gatement/cert_generator)

# 2. transform server .p12 to .keystore:
rm -rf server_keycert_with_cacerts.keystore
keytool -importkeystore -srckeystore ../all_certs/local.com/keycerts.p12 -destkeystore server_keycert_with_cacerts.keystore -srcstorepass 123456 -deststorepass 123456
keytool -list -keystore server_keycert_with_cacerts.keystore -storepass 123456

# 3. transform server cacert.pem to .keystore:
rm -rf server_cacert.keystore
keytool -importcert -noprompt -trustcacerts -file ../all_certs/ServerRootCA/cacert.pem -keystore server_cacert.keystore -storepass 123456
keytool -list -keystore server_cacert.keystore -storepass 123456
