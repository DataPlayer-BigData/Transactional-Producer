#kafka-topics.sh --create --zookeeper localhost:2181 --topic transactional-producer1 --replication-factor 3 --partitions 3 --config min.insync.replicas=2
kafka-topics.sh --create --zookeeper localhost:2181 --topic transactional-producer2 --replication-factor 3 --partitions 3 --config min.insync.replicas=2