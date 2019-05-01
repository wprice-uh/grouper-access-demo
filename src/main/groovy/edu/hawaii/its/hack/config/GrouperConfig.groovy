package edu.hawaii.its.hack.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate

import edu.hawaii.its.hack.grouper.GrouperErrorHandler

import groovy.util.logging.Slf4j

@Slf4j
@Configuration
class GrouperConfig {
  @Value("\${grouper.username}")
  String grouperUsername

  @Value("\${grouper.password}")
  String grouperPassword

  @Value("\${grouper.search.root}")
  String grouperSearchRoot

  @Bean
  RestTemplate grouperTemplate(RestTemplateBuilder builder) {
    builder
        .basicAuthentication(grouperUsername, grouperPassword)
        .errorHandler(grouperErrorHandler())
        .build()
  }

  @Bean
  ResponseErrorHandler grouperErrorHandler() {
    new GrouperErrorHandler()
  }
}
