package ua.nix.multiconfback.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
@EnableConfigurationProperties(ActiveMQProperties.class)
public class BrokerConfig {

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ActiveMQComponent activeMqComponent(
            ActiveMQConnectionFactory activeMQConnectionFactory,
            MessageConverter messageConverter
    ) {
        ActiveMQComponent activeMqComponent = new ActiveMQComponent();
        activeMqComponent.setConnectionFactory(activeMQConnectionFactory);
        activeMqComponent.setMessageConverter(messageConverter);

        return activeMqComponent;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(
            ActiveMQProperties properties
    ) {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(properties.getBrokerUrl());
        activeMQConnectionFactory.setUserName(properties.getUser());
        activeMQConnectionFactory.setPassword(properties.getPassword());

        return activeMQConnectionFactory;
    }

}
