# MySQL Failover Sample

## How to run

    $ docker run --rm -e REPLICATION_MASTER=true -e MYSQL_USER=foo -e MYSQL_PASS=bar -p 3306 --name db0 tutum/mysql:5.5
    $ docker run --rm -e REPLICATION_SLAVE=true -e MYSQL_USER=foo -e MYSQL_PASS=bar -p 3306 --link db0:mysql --name db1 tutum/mysql:5.5
    $ mysql -P `docker port db0 3306/tcp | cut -d : -f 2` -h 127.0.0.1 -u foo -p < tbl.sql
    $ mvn clean package
    $ java -jar target/MysqlReplicationSample-0.0.1.jar 127.0.0.1 `docker port db1 3306/tcp | cut -d : -f 2` 127.0.0.1 `docker port db1 3306/tcp | cut -d : -f 2`
