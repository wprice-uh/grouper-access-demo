package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

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
        .build()
  }
}
