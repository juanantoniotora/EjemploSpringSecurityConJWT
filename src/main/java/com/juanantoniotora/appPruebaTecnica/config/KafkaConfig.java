package com.juanantoniotora.appPruebaTecnica.config;

import com.juanantoniotora.appPruebaTecnica.dto.RegistryRequestKafkaDTO;
import com.juanantoniotora.appPruebaTecnica.model.SearchIdResponse;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${kafka.reply.topic}")
    private String replyTopic;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, RegistryRequestKafkaDTO> producerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, RegistryRequestKafkaDTO> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    private void removeJsonDeserializerProps(Map<String, Object> props) {
        // Evita mezclar configuración por propiedades y por setters
        props.remove(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);
        props.remove(JsonDeserializer.TRUSTED_PACKAGES);
        // eliminar cualquier clave spring.json.* que pudiera venir de kafkaProperties
        props.keySet().removeIf(k -> k != null && k instanceof String && ((String) k).startsWith("spring.json"));
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, SearchIdResponse> replyConsumerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "reply-consumer-group");
        removeJsonDeserializerProps(props);
        JsonDeserializer<SearchIdResponse> deserializer = new JsonDeserializer<>(SearchIdResponse.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public KafkaMessageListenerContainer<String, SearchIdResponse> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(replyConsumerFactory(), containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, RegistryRequestKafkaDTO, SearchIdResponse> replyingKafkaTemplate(
            ProducerFactory<String, RegistryRequestKafkaDTO> pf,
            KafkaMessageListenerContainer<String, SearchIdResponse> replyContainer) {
        ReplyingKafkaTemplate<String, RegistryRequestKafkaDTO, SearchIdResponse> replyingKafkaTemplate =
                new ReplyingKafkaTemplate<>(pf, replyContainer);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        return replyingKafkaTemplate;
    }

    @Bean
    public ProducerFactory<String, SearchIdResponse> replyProducerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, SearchIdResponse> replyKafkaTemplate() {
        return new KafkaTemplate<>(replyProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, RegistryRequestKafkaDTO> requestConsumerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        removeJsonDeserializerProps(props);
        JsonDeserializer<RegistryRequestKafkaDTO> deserializer = new JsonDeserializer<>(RegistryRequestKafkaDTO.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegistryRequestKafkaDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RegistryRequestKafkaDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setReplyTemplate(replyKafkaTemplate());
        return factory;
    }
}
