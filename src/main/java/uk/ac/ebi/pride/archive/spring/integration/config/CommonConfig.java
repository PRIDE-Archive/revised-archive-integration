package uk.ac.ebi.pride.archive.spring.integration.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import uk.ac.ebi.pride.archive.spring.integration.command.handler.DefaultCommandResultHandler;
import uk.ac.ebi.pride.archive.spring.integration.command.runner.DefaultCommandRunner;

/**
 * @author Suresh Hewapathirana
 */
@Configuration
@PropertySource("classpath:command.properties")
public class CommonConfig {

  @Bean
  DefaultCommandResultHandler commandResultHandler(){
    return new DefaultCommandResultHandler();
  }

  @Bean
  public DefaultCommandRunner commandRunner(DefaultCommandResultHandler commandResultHandler){
    return new DefaultCommandRunner(commandResultHandler);
  }

  @Bean(name = "commandConfig")
  public PropertiesFactoryBean commandConfig() {
    PropertiesFactoryBean bean = new PropertiesFactoryBean();
    bean.setLocation(new ClassPathResource("command.properties"));
    return bean;
  }
}
