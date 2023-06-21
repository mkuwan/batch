package net.mkuwan.batch.multijob.api;

import net.mkuwan.batch.multijob.kafka.KafkaConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * kafka-topics.sh --create --bootstrap-server=localhost:9092 --replication-factor 1 --partitions 1 --topic stream-input-topic
 *  kafka-topics.sh --create --bootstrap-server=localhost:9092 --replication-factor 1 --partitions 1 --topic stream-output-topic
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final static Logger logger = LoggerFactory.getLogger(KafkaController.class);

    private static KafkaTemplate<Integer, String> kafkaTemplate = null;


    @Autowired
    public KafkaController(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(path = "/send/{value}")
    public String SendToKafka(@PathVariable String value){
        logger.info("SendToKafka {}", value);
        var future = kafkaTemplate.send(KafkaConfig.KafkaTopicProduce, value);
        future.whenComplete((result, ex) -> {
            if(ex == null){
                logger.info("メッセージ送信に成功しました");
            } else {
                logger.error("メッセージ送信に失敗しました");
            }
        });
//        kafkaTemplate.send("stream-input-topic", "value", "");
        return "OK";
    }

    @PostMapping(path = "/get")
    public String Demo(){
        logger.info("demo put");
//        var received = kafkaTemplate.receive("stream-input-topic", 1, 1);
//        return received.value();
        return "Received!";
    }
}
