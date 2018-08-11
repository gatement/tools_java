# tools_java
tools in java

## Run
* mvn exec:java
* mvn test
* mvn test -Dtest=[ClassName]
* mvn test -Dtest=[ClassName]#[MethodName(could include wild card '*')]

# Test config
* java 1.8
* nginx 1.13.12, config: ./nginx.conf, url: https://local.com:40443
* /etc/hosts: 127.0.0.1 local.com

done:
* mvn test -Dtest=johnson.tools.protobuf.NettyProtobufEchoServerTest
* mvn test -Dtest=johnson.tools.encryption.asymmetry.DHUtilTest
* mvn test -Dtest=johnson.tools.encryption.asymmetry.DSAUtilTest
* mvn test -Dtest=johnson.tools.encryption.asymmetry.RSAUtilTest
* mvn test -Dtest=johnson.tools.encryption.hash.HashUtilTest
* mvn test -Dtest=johnson.tools.encryption.symmetry.AESUtilTest
* mvn test -Dtest=johnson.tools.encryption.symmetry.PBEUtilTest
* mvn test -Dtest=johnson.tools.encryption.pki.CertificateUtilTest
* mvn test -Dtest=johnson.tools.encryption.pki.HttpsClientTest

todo:
* mvn test -Dtest=johnson.tools.encryption.pki.NettyHttpsClientTest
* mvn test -Dtest=johnson.tools.encryption.pki.NettyHttpsServerTest
* mvn test -Dtest=johnson.tools.encryption.pki.NettySslServerTest
* mvn test -Dtest=johnson.tools.encryption.pki.PrintSSLSocketCertificateTest
* mvn test -Dtest=johnson.tools.encryption.pki.SSLSocketServerTest