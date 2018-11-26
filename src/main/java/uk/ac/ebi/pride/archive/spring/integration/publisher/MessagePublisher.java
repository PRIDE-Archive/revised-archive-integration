package uk.ac.ebi.pride.archive.spring.integration.publisher;

/**
 * @author Suresh Hewapathirana
 */

/**
 * MessagePublisher is an interface to sending message notification
 * Q: represents the address where the message will be sent
 */
public interface MessagePublisher<Q> {
   <T> void sendNotification(Q queue, T payload, Class<T> payloadClass);
}