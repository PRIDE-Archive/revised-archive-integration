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
import uk.ac.ebi.pride.archive.spring.integration.message.model.impl.AssayDataGenerationPayload;

import java.util.Collection;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
@MessageEndpoint
@PropertySource("classpath:application.properties")
public class AssayAnalyseService {

  @Autowired
  private CommandRunner assayAnalyseCommandRunner;

  @Value("${command.assay.analyse.command}")
  private String assayAnalyseCommand;

  @ServiceActivator(inputChannel ="assayAnalyseChannel")
  public void analseAssay(AssayDataGenerationPayload dataGenerationPayload) {

    log.info("Running Assay Analyse job to insert data to MongoDB: " + dataGenerationPayload.getAccession());

    CommandBuilder assayAnalyseCommandBuilder = new DefaultCommandBuilder();

    assayAnalyseCommandBuilder.argument(assayAnalyseCommand);
    // append project accession
    assayAnalyseCommandBuilder.argument("-p", dataGenerationPayload.getAccession());
    // append assay accession
    assayAnalyseCommandBuilder.argument("-a", dataGenerationPayload.getAssayAccession());

    Collection<String> command = assayAnalyseCommandBuilder.getCommand();
    assayAnalyseCommandRunner.run(command);
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}

