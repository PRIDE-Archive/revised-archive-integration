package uk.ac.ebi.pride.archive.spring.integration.config;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.redis.inbound.RedisQueueMessageDrivenEndpoint;
import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.AssayDataGenerationPayload;

/** @author Suresh Hewapathirana */
@Log
@Configuration
@EnableIntegration
@Order(3)
public class AssayAnalyseServiceConfig {

  @Autowired
  JedisConnectionFactory jedisConnectionFactory;

  @Bean
  public Jackson2JsonRedisSerializer<AssayDataGenerationPayload> postPublicationJsonRedisSerializer() {
    return  new Jackson2JsonRedisSerializer<>(AssayDataGenerationPayload.class);
  }

  @Bean
  RedisQueueMessageDrivenEndpoint redisQueueMessageDrivenEndpoint(JedisConnectionFactory jedisConnectionFactory) {
    RedisQueueMessageDrivenEndpoint endpoint =
        new RedisQueueMessageDrivenEndpoint("archive.incoming.assay.annotation.queue", jedisConnectionFactory);
    endpoint.setOutputChannelName("assayAnalyseChannel");
    endpoint.setErrorChannelName("assayAnalyseLoggingChannel");
    endpoint.setReceiveTimeout(5000);
    endpoint.setSerializer(new Jackson2JsonRedisSerializer<>(AssayDataGenerationPayload.class));
    return endpoint;
  }

  @Bean
  public DirectChannel assayAnalyseChannel() {
    return new DirectChannel();
  }

  @Bean
  public DirectChannel assayAnalyseLoggingChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow flow(JedisConnectionFactory jedisConnectionFactory) {
    return IntegrationFlows.from(redisQueueMessageDrivenEndpoint(jedisConnectionFactory))
        .handle("AssayAnalyseService", "analseAssay")
        .log()
        .get();
  }
}
