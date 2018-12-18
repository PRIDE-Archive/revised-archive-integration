package uk.ac.ebi.pride.archive.spring.integration.service.reciever;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import uk.ac.ebi.pride.archive.spring.integration.command.builder.CommandBuilder;
import uk.ac.ebi.pride.archive.spring.integration.command.builder.DefaultCommandBuilder;
import uk.ac.ebi.pride.archive.spring.integration.command.runner.CommandRunner;
import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.PublicationCompletionPayload;

import java.util.Collection;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
@MessageEndpoint
@PropertySource("classpath:application.properties")
public class PostPublicationService {

  @Autowired
  private CommandRunner postPublicationCommandRunner;

  @Value("${command.post.publication.command}")
  private String postPublicationCommand;

  @Value("${command.post.reset.publication.command}")
  private String postResetPublicationCommand;


  @ServiceActivator(inputChannel ="postPublicationChannel")
  public void SyncWithMongoDB(PublicationCompletionPayload submission) {

    log.info("Running Post Publication job to insert data to MongoDB: " + submission.getAccession());

    CommandBuilder postPublicationCommandBuilder = new DefaultCommandBuilder();

    final String commandScript = (submission.getAccession().contains("_ERROR"))? postResetPublicationCommand : postPublicationCommand ;
    postPublicationCommandBuilder.argument(commandScript);
    // append project accession
    postPublicationCommandBuilder.argument("-p", submission.getAccession());
    // append notification argument
    postPublicationCommandBuilder.argument("-n");

    Collection<String> command = postPublicationCommandBuilder.getCommand();
    postPublicationCommandRunner.run(command);
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}

