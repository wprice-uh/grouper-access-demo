package edu.hawaii.its.hack.config

import java.nio.charset.StandardCharsets
import java.util.function.Supplier

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import edu.hawaii.its.hack.grouper.GrouperErrorHandler

import groovy.util.logging.Slf4j

/**
 * Grouper-related context configuration
 */
@Slf4j
@Configuration
class GrouperConfig {
  @Value("\${grouper.username}")
  String grouperUsername

  @Value("\${grouper.password}")
  String grouperPassword

  @Bean
  RestTemplate grouperTemplate(RestTemplateBuilder builder) {
    log.debug 'instantiating grouperTemplate'

    builder
        .basicAuthentication(grouperUsername, grouperPassword)
        .errorHandler(grouperErrorHandler())
        .requestFactory(grouperRequestFactorySupplier())
        .additionalMessageConverters(grouperConverter())
        .build()
  }

  @Bean
  MappingJackson2HttpMessageConverter grouperConverter() {
    new MappingJackson2HttpMessageConverter().tap {
      supportedMediaTypes = [new MediaType('text', 'x-json', StandardCharsets.UTF_8)]
      objectMapper = grouperObjectMapper()
    }
  }

  @Bean
  ObjectMapper grouperObjectMapper() {
    new ObjectMapper().tap {
      configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
      configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
      configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true)
    }
  }

  @Bean
  ResponseErrorHandler grouperErrorHandler() {
    log.debug 'instantiating grouperErrorHandler'

    new GrouperErrorHandler(grouperObjectMapper())
  }

  // had to create a supplier to wrap the connectionPoolingFactory because
  // the compiler was getting confused
  @Bean
  Supplier<ClientHttpRequestFactory> grouperRequestFactorySupplier() {
    log.debug 'instantiating grouperRequestFactorySupplier'

    new Supplier() {
      @Override
      ClientHttpRequestFactory get() {
        grouperConnectionPoolingFactory()
      }
    }
  }

  @Bean
  HttpComponentsClientHttpRequestFactory grouperConnectionPoolingFactory() {
    log.debug 'instantiating grouperConnectionPoolingFactory'

    new HttpComponentsClientHttpRequestFactory()
  }
}
