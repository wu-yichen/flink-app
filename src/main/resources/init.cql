CREATE KEYSPACE IF NOT EXISTS cassandrasink WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1} ;
USE cassandrasink;
CREATE TABLE IF NOT EXISTS messages ( payload text PRIMARY KEY );