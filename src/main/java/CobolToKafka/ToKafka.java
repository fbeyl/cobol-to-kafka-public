package CobolToKafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
//import java.util.Date;
import java.nio.ByteBuffer;
//import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
//import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.Producer;
import java.util.Properties;

public class ToKafka {

    private static int i;
    private static String os = System.getProperty("os.name");
    private static String brokers = System.getenv("CLOUDKARAFKA_BROKERS");
    private static String username = System.getenv("CLOUDKARAFKA_USERNAME");
    private static String password = System.getenv("CLOUDKARAFKA_PASSWORD");
    private static String topic = System.getenv("CLOUDKARAFKA_USERNAME") + "-dataset";
    private static String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
    private static String jaasCfg = String.format(jaasTemplate, username, password);
//    private static String serializer = StringSerializer.class.getName();
    private static String serializer = ByteArraySerializer.class.getName();
//    private static String deserializer = StringDeserializer.class.getName();
//    private static Producer<String,String> kp;
    private static Producer<byte[],byte[]> kp;
    private static Properties props = (Properties) new Properties();

public static void toKafkaSend(final ByteBuffer cobData) {
    //final Date d = new Date();
    cobData.position(0);
    switch (cobData.get()) {
        case (byte)0xF1: {
            cobData.limit(10);
            break;
        }
        case (byte)0xF2: {
            cobData.limit(3);
            break;
        }
        case (byte)0xF3: {
            cobData.limit(16);
            break;
        }
        case (byte)0xF4: {
            cobData.limit(3100);
            break;
        }
        case (byte)0xF9: {
            cobData.limit(1);
            break;
        }
        default: {
            cobData.limit(1);
            cobData.position(0);
            cobData.put((byte)0xFF);
            break;
        }
    }
    cobData.position(0);
    try {
        final byte[] cobDataArray = new byte[cobData.limit()];
        for (int j = 0; j < cobData.limit(); ++j) {
            cobDataArray[j] = cobData.get();
        }
        kp.send(new ProducerRecord<>(topic, cobDataArray)).get();
    }
    catch (Exception e) {
        System.out.println("Ack " + i + " not received");
        e.printStackTrace();
    }
    ++i;
}

public static void toKafkaConnect() {

    props.setProperty("bootstrap.servers", brokers);
    props.setProperty("group.id", username + "-" + os);
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "5000");
    props.setProperty("auto.offset.reset", "latest");
    props.setProperty("session.timeout.ms", "60000");
    //props.setProperty("key.deserializer", deserializer);
    //props.setProperty("value.deserializer", deserializer);
    props.setProperty("key.serializer", serializer);
    props.setProperty("value.serializer", serializer);
    props.setProperty("security.protocol", "SASL_SSL");
    props.setProperty("sasl.mechanism", "SCRAM-SHA-256");
    props.setProperty("sasl.jaas.config", jaasCfg);
    props.setProperty("acks", "all");
    props.setProperty("batch.size", "10");

    i = 1;
    kp = new KafkaProducer<>(props);
}

public static void toKafkaDisconnect() {
   kp.close();
}
}