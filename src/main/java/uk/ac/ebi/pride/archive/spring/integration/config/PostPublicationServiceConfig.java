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
import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.PublicationCompletionPayload;

/** @author Suresh Hewapathirana */
@Log
@Configuration
@EnableIntegration
@Order(2)
public class PostPublicationServiceConfig {

  @Autowired
  JedisConnectionFactory jedisConnectionFactory;

  @Bean
  public Jackson2JsonRedisSerializer<PublicationCompletionPayload> postPublicationJsonRedisSerializer() {
    return  new Jackson2JsonRedisSerializer<>(PublicationCompletionPayload.class);
  }

  @Bean
  RedisQueueMessageDrivenEndpoint postPublicationRedisQueueMessageDrivenEndpoint(JedisConnectionFactory jedisConnectionFactory) {
    RedisQueueMessageDrivenEndpoint endpoint =
        new RedisQueueMessageDrivenEndpoint("archive.post.publication.completion.queue", jedisConnectionFactory);
    endpoint.setOutputChannelName("postPublicationChannel");
    endpoint.setErrorChannelName("postPublicationLoggingChannel");
    endpoint.setReceiveTimeout(5000);
    endpoint.setSerializer(postPublicationJsonRedisSerializer());
    return endpoint;
  }

  @Bean
  public DirectChannel postPublicationChannel() {
    return new DirectChannel();
  }

  @Bean
  public DirectChannel postPublicationLoggingChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow postPublicationFlow(JedisConnectionFactory jedisConnectionFactory) {
    return IntegrationFlows.from(postPublicationRedisQueueMessageDrivenEndpoint(jedisConnectionFactory))
        .handle("PostPublicationService", "syncWithMongoDB")
        .log()
        .get();
  }
}
