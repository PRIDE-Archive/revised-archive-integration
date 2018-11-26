//package uk.ac.ebi.pride.archive.spring.integration.publisher;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPubSub;
//import uk.ac.ebi.pride.archive.dataprovider.project.SubmissionType;
//import uk.ac.ebi.pride.archive.spring.integration.config.RedisConfiguration;
//import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.IncomingSubmissionPayload;
//import uk.ac.ebi.pride.archive.spring.integration.subscriber.RedisMessageSubscriber;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.concurrent.CountDownLatch;
//
///** @author Suresh Hewapathirana */
//@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = RedisConfiguration.class)
//@SpringBootTest(classes = {RedisMessageNotifierTest.class})
//public class RedisMessageNotifierTest {
//
//  @Autowired JedisCluster jedisCluster;
//  @Autowired MessagePublisher redisMessagePublisher;
//  private CountDownLatch messageReceivedLatch = new CountDownLatch(1);
//  private ArrayList<String> messageContainer = new ArrayList<>();
//
//  @Before
//  public void setUp() throws Exception {}
//
//  @Test
//  public void messagePubSubTest() throws Exception {
//
//    final String ERROR_SUFFIX = "_ERROR";
//    final String TICKET_AND_ID = "1-20181010-1234";
//    final String TICKET_AND_ID_ERROR = TICKET_AND_ID + ERROR_SUFFIX;
//    final String jedisChannel = "archive.incoming.submission.queue";
//
//    redisMessagePublisher.sendNotification(
//            jedisChannel,
//            new IncomingSubmissionPayload(TICKET_AND_ID, SubmissionType.COMPLETE, Calendar.getInstance().getTime()),
//            IncomingSubmissionPayload.class);
//    log.info("Message published " + TICKET_AND_ID);
//
//    JedisPubSub jedisPubSub = RedisMessageSubscriber.setupSubscriber(
//            TICKET_AND_ID,
//            TICKET_AND_ID_ERROR,
//            messageReceivedLatch,
//            messageContainer,
//            jedisCluster,
//            jedisChannel);
//    messageReceivedLatch.await();
//    final String MESSAGE_RECEIVED = messageContainer.iterator().next();
//    log.info("Received REDIS message: " + MESSAGE_RECEIVED);
//    jedisPubSub.unsubscribe();
//  }
//
//  @After
//  public void tearDown() throws Exception {}
//}
