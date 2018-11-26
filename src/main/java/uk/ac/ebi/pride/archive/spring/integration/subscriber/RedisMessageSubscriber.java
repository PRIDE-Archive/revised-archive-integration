package uk.ac.ebi.pride.archive.spring.integration.subscriber;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Suresh Hewapathirana
 */
@Service
@Slf4j
@Getter
public class RedisMessageSubscriber {

  public static JedisPubSub setupSubscriber(String messageToWatchFor, String messageWithError,
                                            CountDownLatch messageReceivedLatch, ArrayList<String> messageContainer,
                                            JedisCluster jedisCluster, String jedisChannel) {
    log.info("Good message to watch for: " + messageToWatchFor);
    log.info("Error message to watch for: " + messageWithError);
    final JedisPubSub jedisPubSub = new JedisPubSub() {

      public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info("Unsubscribed to " + channel + " JEDIS channel for " + messageToWatchFor + ".");
      }

      public void onSubscribe(String channel, int subscribedChannels) {
        log.info("Subscribed to "+ channel +" JEDIS channel to watch for: " + messageToWatchFor);
      }

      public void onMessage(String channel, String message) {
        if (message.equalsIgnoreCase(messageWithError)) {
          log.info("Received error message for: " + message);
          messageContainer.add(message);
          messageReceivedLatch.countDown();
        } else if (message.equalsIgnoreCase(messageToWatchFor)) {
          log.info("Received OK message for: " + messageToWatchFor);
          messageContainer.add(message);
          messageReceivedLatch.countDown();
        }
      }
    };
    new Thread(() -> {
      try {
        log.info("Subscribing to JEDIS for " + messageToWatchFor);
        jedisCluster.subscribe(jedisPubSub, jedisChannel);
        jedisCluster.close();
      } catch (Exception e) {
        log.error("Exception when subscribing to JEDIS for " + messageToWatchFor, e);
      }
    }, "subscriberThread").start();
    return jedisPubSub;
  }
}