package tu.cit.examples.kafkaapi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class TransactionalProducer {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"my-transactional-producer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"my-transaction-id-t1");

        KafkaProducer<Integer,String> producer = new KafkaProducer<Integer, String>(props);
        producer.initTransactions();

        logger.info("Starting first transaction...");
        producer.beginTransaction();
        try {
            for (int i = 0; i < 2; i++) {
                logger.info("Start Producing Message : " + i);
                ProducerRecord record1 = new ProducerRecord("transactional-producer1", i, "Simple Message-T2 : " + i);
                ProducerRecord record2 = new ProducerRecord("transactional-producer2", i, "Simple Message-T2 : " + i);
                producer.send(record1);
                logger.info("Message : " + i + "Produced to transactional-producer1");
                Thread.sleep(20000);
                producer.send(record2);
                logger.info("Message : " + i + "Produced to transactional-producer2");
            }
            logger.info("Committing First Transaction");
            producer.commitTransaction();
        }catch(Exception e){
            logger.error("Exception in First Transcation. Aborting...");
            producer.abortTransaction();
            producer.close();
            throw new RuntimeException(e);
        }


        //Without TRANSACTION ID
//        logger.info("Starting first transaction...");
//        for (int i = 0; i < 2; i++) {
//            logger.info("Start Producing Message : " + i);
//            ProducerRecord record1 = new ProducerRecord("transactional-producer1", i, "Simple Message-T1 : " + i);
//            ProducerRecord record2 = new ProducerRecord("transactional-producer2", i, "Simple Message-T1 : " + i);
//
//            try{
//                producer.send(record1);
//                logger.info("Message : " + i + "Produced to transactional-producer1");
//
//                Thread.sleep(30000);
//
//                producer.send(record2);
//                logger.info("Message : " + i + "Produced to transactional-producer2");
//
//            }catch(Exception e){e.printStackTrace();}
//
//        }
    }
}
