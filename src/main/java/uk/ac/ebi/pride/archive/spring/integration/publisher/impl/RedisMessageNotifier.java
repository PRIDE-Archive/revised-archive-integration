package uk.ac.ebi.pride.archive.spring.integration.publisher.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import uk.ac.ebi.pride.archive.spring.integration.config.RedisConfiguration;
import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.AssayDataGenerationPayload;
import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.PublicationCompletionPayload;
import uk.ac.ebi.pride.archive.spring.integration.publisher.MessagePublisher;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
public class RedisMessageNotifier implements MessagePublisher<String>{

  private RedisConnectionFactory redisConnectionFactory;

  /**
   * Constructor, sets the Redis connection factory
   * @param redisConnectionFactory the Redis connection factory
   */
  public RedisMessageNotifier(RedisConnectionFactory redisConnectionFactory) {
    Assert.notNull(redisConnectionFactory, "Redis connection factory cannot be null");
    this.redisConnectionFactory = redisConnectionFactory;
  }

  /**
   * Send a new submission notification to a given queue
   * @param queue submission queue
   * @param payload pay load
   */
  public <T> void sendNotification(String queue, T payload, Class<T> payloadClass) {
    RedisTemplate<String, T> submissionRedisTemplate = getRedisTemplate(redisConnectionFactory, payloadClass);
    ListOperations<String, T> submissionList = submissionRedisTemplate.opsForList();
    submissionList.leftPush(queue, payload);
    log.info("payload: "+ payload.toString() + " received to queue " + queue);
  }

  /**
   * Create an redis template from a given redis connection factory and a given message type
   */
  private <P> RedisTemplate<String, P> getRedisTemplate(RedisConnectionFactory redisConnectionFactory, Class<P> type) {
    RedisTemplate<String, P> publicationRedisTemplate = new RedisTemplate<>();
    publicationRedisTemplate.setConnectionFactory(redisConnectionFactory);
    publicationRedisTemplate.setKeySerializer(new StringRedisSerializer());
    publicationRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(type));
    publicationRedisTemplate.afterPropertiesSet(); // must call this before use
    return publicationRedisTemplate;
  }

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(RedisConfiguration.class);
    RedisConnectionFactory connectionFactory = context.getBean("jedisConnectionFactory", RedisConnectionFactory.class);
    RedisMessageNotifier messageGenerator = new RedisMessageNotifier(connectionFactory);
    final int arg = Integer.parseInt(args[0]);

    switch (arg) {
      case 7:
        messageGenerator.sendNotification("archive.post.publication.completion.queue",
                // projectAccession
                new PublicationCompletionPayload(args[1]), PublicationCompletionPayload.class);
      case 8:
        messageGenerator.sendNotification("archive.incoming.assay.annotation.queue",
                // projectAccession , assayAccession;
                new AssayDataGenerationPayload(args[1],args[2]), AssayDataGenerationPayload.class);
        break;
      default:
        break;
    }
  }
}
