mkdir /opt/cassandra
sudo mkdir /opt/cassandra
sudo mkdir /opt/java
tar xfz downloads/apache-cassandra-3.9-bin.tar.gz -C /opt/cassandra/
sudo tar xfz downloads/apache-cassandra-3.9-bin.tar.gz -C /opt/cassandra/
sudo tar xfz downloads/server-jre-8u112-linux-x64.tar.gz -C /opt/java/
cp downloads/jna/dist/jna.jar /usr/share/java/jna.jar
sudo cp downloads/jna/dist/jna.jar /usr/share/java/jna.jar
sudo cp downloads/jna/dist/jna-platform.jar /usr/share/java/jna-platform.jar
sudo mkdir /opt/cassandra/lib
sudo ln -s /usr/share/java/jna.jar /opt/cassandra/lib/jna.jar
sudo ln -s /usr/share/java/jna-platform.jar /opt/cassandra/lib/jna-platform.jar
export JAVA_HOME=/opt/java/jdk1.8.0_112
export CASS_HOME=/opt/cassandra/apache-cassandra-3.9/
export PATH=$PATH:$JAVA_HOME/bin:$CASS_HOME/bin/
sudo mkdir /var/lib/cassandra
sudo mkdir /var/log/cassandra
sudo chown -R vladvin /var/lib/cassandra
sudo chown -R vladvin /var/log/cassandra
cd /opt/cassandra/apache-cassandra-3.9/
sudo chown -R vladvin .
bin/cassandra
nodetool status
cqlsh
cqlsh 10.0.0.4 9042 --cqlversion="3.4.0"
