1. generate the server certificate and client certificate by cer_generator(https://github.com/gatement/cert_generator)

2. transform server .p12 to .keystore:
keytool -importkeystore -srckeystore ../all_certs/local.com/keycert.p12 -destkeystore server_keycert_with_intermediate_ca.keystore -srcstorepass 123456 -deststorepass 123456
keytool -importcert -noprompt -trustcacerts -file ../all_certs/ServerIntermediateCA/cacert.pem -keystore server_keycert_with_intermediate_ca.keystore -storepass 123456
keytool -list -keystore server_keycert_with_intermediate_ca.keystore -storepass 123456

3. transform client cacert.pem to .keystore:
keytool -importcert -noprompt -trustcacerts -file ../all_certs/ClientRootCA/cacert.pem -keystore client_cacert.keystore -storepass 123456
keytool -list -keystore client_cacert.keystore -storepass 123456
