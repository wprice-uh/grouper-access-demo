package edu.hawaii.its.hack.config

import java.nio.charset.StandardCharsets
import java.util.function.Supplier

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.BufferingClientHttpRequestFactory
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

  /**
   * @return RestTemplate configured to invoke grouper
   */
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

  /**
   * @return messageConverter with support for the non-standard MediaType
   * returned by grouper
   */
  @Bean
  MappingJackson2HttpMessageConverter grouperConverter() {
    log.debug 'instantiating grouperConverter'

    new MappingJackson2HttpMessageConverter().tap {
      supportedMediaTypes = [new MediaType('text', 'x-json', StandardCharsets.UTF_8)]
      objectMapper = grouperObjectMapper()
    }
  }

  /**
   * @return ObjectMapper configured for processing grouper responses
   */
  @Bean
  ObjectMapper grouperObjectMapper() {
    log.debug 'instantiating grouperObjectMapper'

    new ObjectMapper().tap {
      // ignore unrecognized properties, so that my json model classes need
      // only contain values I'm actively interested in using
      configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)

      // unwrap the top-level object, to simplify accessing subcomponents
      configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)

      // allow single-value arrays to be flattened into that single value,
      // to simplify accessing subcomponents
      configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true)
    }
  }

  /**
   * @return ResponseErrorHandler to prevent some 500 errors from throwing
   * uselessly-generic exceptions
   */
  @Bean
  ResponseErrorHandler grouperErrorHandler() {
    log.debug 'instantiating grouperErrorHandler'

    new GrouperErrorHandler(grouperObjectMapper())
  }

  /**
   * @return ClientHttpRequestFactory required to plug grouperRequestFactory
   * into the RestTemplate infrastructure
   */
  @Bean
  Supplier<ClientHttpRequestFactory> grouperRequestFactorySupplier() {
    log.debug 'instantiating grouperRequestFactorySupplier'

    new Supplier() {
      @Override
      ClientHttpRequestFactory get() {
        grouperRequestFactory()
      }
    }
  }

  /**
   * Replaces the default factory with one which uses apache's HttpComponents,
   * to gain more configurability. Wraps the configured factory with a wrapper
   * which supports reading the body multiple times, since I need that to
   * parse responses having 500 status (see {@link GrouperErrorHandler})
   * without breaking further response-processing
   */
  @Bean
  ClientHttpRequestFactory grouperRequestFactory() {
    log.debug 'instantiating grouperRequestFactory'

    new BufferingClientHttpRequestFactory(
        new HttpComponentsClientHttpRequestFactory()
    )
  }
}
