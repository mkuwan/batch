package net.mkuwan.batch.multijob.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * topic作成
 * kafka-topics.sh --create --bootstrap-server=localhost:9092 --replication-factor 1 --partitions 1 --topic multi-job-topic-produce
 * kafka-topics.sh --create --bootstrap-server=localhost:9092 --replication-factor 1 --partitions 1 --topic multi-job-topic-consume
 *
 * https://spring.pleiades.io/spring-kafka/docs/current/reference/html/#kafka-template
 */

@Configuration
@EnableKafka
//@EnableKafkaStreams
public class KafkaConfig {
    private final static Logger logger = LoggerFactory.getLogger(KafkaConfig.class);
    public static final String ApplicationIdConfig = "job-streams-app";
    public static final String BootstrapServer = "localhost:9092";
    public static final String KafkaTopicProduce = "multi-job-topic-produce";
    public static final String KafkaTopicConsume = "multi-job-topic-consume";

    @Bean
    public KafkaAdmin admin(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(KafkaTopicProduce)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("thing1")
                .partitions(10)
                .replicas(3)
                .compact()
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("thing2")
                .partitions(10)
                .replicas(3)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    @Bean
    public NewTopic topic3() {
        return TopicBuilder.name("thing3")
                .assignReplicas(0, Arrays.asList(0, 1))
                .assignReplicas(1, Arrays.asList(1, 2))
                .assignReplicas(2, Arrays.asList(2, 0))
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    @Bean
    public NewTopic topic4() {
        return TopicBuilder.name("defaultBoth")
                .build();
    }

    @Bean
    public NewTopic topic5() {
        return TopicBuilder.name("defaultPart")
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic6() {
        return TopicBuilder.name("defaultRepl")
                .partitions(3)
                .build();
    }

    @Bean
    public KafkaAdmin.NewTopics topics456() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("defaultBoth")
                        .build(),
                TopicBuilder.name("defaultPart")
                        .replicas(1)
                        .build(),
                TopicBuilder.name("defaultRepl")
                        .partitions(3)
                        .build());
    }

    // プロデューサー
    @Bean
    public ProducerFactory<Integer, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return props;
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate() {
        return new KafkaTemplate<Integer, String>(producerFactory());
    }

    // リスナー
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return props;
    }
}
