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