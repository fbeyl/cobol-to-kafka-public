package CobolToKafka;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;


public class FromKafkaToPdf {

    private static String filename = "kafka-output.pdf";
    private static String os = System.getProperty("os.name");
    private static String brokers = System.getenv("CLOUDKARAFKA_BROKERS");
    private static String username = System.getenv("CLOUDKARAFKA_USERNAME");
    private static String password = System.getenv("CLOUDKARAFKA_PASSWORD");
    private static String topic = System.getenv("CLOUDKARAFKA_USERNAME") + "-dataset";
    private static String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
    private static String jaasCfg = String.format(jaasTemplate, username, password);
    private static Properties props = (Properties) new Properties();
    public static void main(String[] args) throws IOException {

      System.out.println("Entering main");
      props.setProperty("bootstrap.servers", brokers);
      props.setProperty("group.id", username + "-" + os);
      props.setProperty("enable.auto.commit", "true");
      props.setProperty("auto.commit.interval.ms", "5000");
      props.setProperty("auto.offset.reset", "earliest");
      props.setProperty("session.timeout.ms", "60000");
      props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
      props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
      props.setProperty("security.protocol", "SASL_SSL");
      props.setProperty("sasl.mechanism", "SCRAM-SHA-256");
      props.setProperty("sasl.jaas.config", jaasCfg);
      props.setProperty("acks", "all");
      props.setProperty("batch.size", "1000");
      KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props);
      consumer.subscribe(Arrays.asList(topic));
      System.out.println("Consumer subscribed to " + topic);
      KafkaRecType1 fromkafkaRec1 = new KafkaRecType1();
      KafkaRecType2 fromkafkaRec2 = new KafkaRecType2();
      KafkaRecType3 fromkafkaRec3 = new KafkaRecType3();
      KafkaRecType4 fromkafkaRec4 = new KafkaRecType4();
      PDDocument doc = new PDDocument();
      boolean datasetLoop = true;
      int pages = 0;
      while (datasetLoop) {
        ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<byte[], byte[]> record : records) {
          PDPage page = new PDPage();
          doc.addPage(page);
          PDFont font = PDType1Font.HELVETICA;
          PDPageContentStream contents = new PDPageContentStream(doc, page);
          contents.beginText();
          contents.setFont(font, 12);
          contents.setLeading(14.5f);
          contents.newLineAtOffset(25, 725);

          contents.showText("Topic: " + record.topic());
//          contents.setFont(bodyfont, bodysize);
          String message = "";
          switch (record.value()[0]) {
              case (byte)0xF1:
                fromkafkaRec1.setBytes(record.value());
                message = "Recordtype 1: " + String.valueOf(fromkafkaRec1.getType1Field1());
                break;
              case (byte)0xF2:
                fromkafkaRec2.setBytes(record.value());
                message = "Recordtype 2: " + String.valueOf(fromkafkaRec2.getType2Field1());
                break;
              case (byte)0xF3:
                fromkafkaRec3.setBytes(record.value());
                message = "Recordtype 3: " + String.valueOf(fromkafkaRec3.getType3Field1());
                break;
              case (byte)0xF4:
                fromkafkaRec4.setBytes(Arrays.copyOfRange(record.value(),0,3));
                byte[] jsonArray = Arrays.copyOfRange(record.value(),3,fromkafkaRec4.getType4Field1() + 3 );
                String json = new String(jsonArray, StandardCharsets.UTF_8);
                message = "Recordtype 4: " + json;
                break;
              case (byte)0xF9:
                message = "EOF record reached";
                datasetLoop = false;
                break;
              case (byte)0xFF:
                System.out.println("Wrong recordtype encountered");
                datasetLoop = false;
                break;
              default:
                System.out.println("Dataset aborted");
                datasetLoop = false;
          }
          if (message != "") {
            pages++;
            contents.newLine();
            contents.showText("Partition: " + String.valueOf(record.partition()));
            contents.newLine();
            contents.showText("Offset: " + String.valueOf(record.offset()));
            contents.newLine();
            contents.showText("Os: " + os);
            contents.setFont(font, 16);
            int i = message.length();
            int a = 0;
            while (i > 0) {
              a = a + 1;
              contents.newLine();
              if (i > 62) {
                contents.showText(message.substring((a*63)-63,(a*63)-1));
              }
              else {
                contents.showText(message.substring((a*63)-63));
              };
              i = i - 63;
            };
          };
          contents.endText();
          contents.close();
        }
      }
      doc.save(new File(filename));
      doc.close();
      System.out.println("Pages: " + pages);
      System.out.println("File: " + filename + " saved");
      System.out.println("Closing consumer");
      consumer.close();
    }
}
