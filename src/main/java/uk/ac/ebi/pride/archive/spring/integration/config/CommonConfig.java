package uk.ac.ebi.pride.archive.spring.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.pride.archive.spring.integration.command.handler.DefaultCommandResultHandler;
import uk.ac.ebi.pride.archive.spring.integration.command.runner.DefaultCommandRunner;

/**
 * @author Suresh Hewapathirana
 */
@Configuration
public class CommonConfig {

  @Bean
  DefaultCommandResultHandler commandResultHandler(){
    return new DefaultCommandResultHandler();
  }

  @Bean
  public DefaultCommandRunner commandRunner(DefaultCommandResultHandler commandResultHandler){
    return new DefaultCommandRunner(commandResultHandler);
  }
}
