#!bin/bash
mvn -o clean install
rm ../../wso2esb-3.0.1/repository/components/dropins/node-manager-1.0-SNAPSHOT.jar
rm ../../another-wso2esb-3.0.1/repository/components/dropins/node-manager-1.0-SNAPSHOT.jar
rm ../../yet-another-wso2esb-3.0.1/repository/components/dropins/node-manager-1.0-SNAPSHOT.jar
cp target/node-manager-1.0-SNAPSHOT.jar ../../wso2esb-3.0.1/repository/components/dropins/
cp target/node-manager-1.0-SNAPSHOT.jar ../../another-wso2esb-3.0.1/repository/components/dropins/
cp target/node-manager-1.0-SNAPSHOT.jar	../../yet-another-wso2esb-3.0.1/repository/components/dropins/
