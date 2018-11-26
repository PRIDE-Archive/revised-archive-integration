package uk.ac.ebi.pride.archive.spring.integration.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
public class Util {

  private static String STRING_SEPARATOR = "##";

  public static Set<HostAndPort>formatHostPort(String jedisServer, String jedisPort){
    String[] servers;
    String[] ports;
    Set<HostAndPort> jedisClusterNodes = new HashSet<>();

    try {
      if (jedisServer.contains(STRING_SEPARATOR)) {
        servers = jedisServer.split(STRING_SEPARATOR);
        if (jedisPort.contains(STRING_SEPARATOR)) {
          ports = jedisPort.split(STRING_SEPARATOR);
        } else {
          ports = new String[]{jedisPort};
        }
        if (ports.length!=1 && ports.length!=servers.length) {
          log.error("Mismatch between provided Redis ports and servers. Should either have 1 port for all servers, or 1 port per server");
        }
        for (int i=0; i<servers.length; i++) {
          String serverPort = ports.length == 1 ? ports[0] : ports[i];
          jedisClusterNodes.add(new HostAndPort(servers[i], Integer.parseInt(serverPort)));
          log.info("Added Jedis node: " + servers[i] + " " + serverPort);
        }
      } else {
        jedisClusterNodes.add(new HostAndPort(jedisServer, Integer.parseInt(jedisPort))); //Jedis Cluster will attempt to discover cluster nodes automatically
        log.info("Added Jedis node: " + jedisServer + " " + jedisPort);
      }
    } catch (NumberFormatException e) {
      log.error("Redis Host and Port numbers mapping issue found:" + e.getMessage());
    }
    return jedisClusterNodes;
  }

  public static ChannelTopic getQueue(MessageQueue MessageQueue){
    switch(MessageQueue) {
      case INCOMMING_SUBMISSION:
        return new ChannelTopic("archive.incoming.submission.queue.test");
      case MZTAB_COMPLETION:
        return new ChannelTopic("archive.mztab.completion.queue");
      case MGF_COMPLETION:
        return new ChannelTopic("archive.mgf.completion.queue");
      case POST_SUBMISSION_COMPLETION:
        return new ChannelTopic("archive.post.submission.completion.queue");
      default:
        return null;
    }
  }
}

