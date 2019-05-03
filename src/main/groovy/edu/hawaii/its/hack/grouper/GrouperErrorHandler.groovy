package edu.hawaii.its.hack.grouper

import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import com.fasterxml.jackson.databind.ObjectMapper

import groovy.util.logging.Slf4j

/**
 * Parses all rest responses, to prevent things like "uhuuid unknown" from
 * throwing an exception instead of returning an empty group-membership
 * list
 */
@Slf4j
class GrouperErrorHandler extends DefaultResponseErrorHandler {
  final ObjectMapper om

  GrouperErrorHandler(ObjectMapper objectMapper) {
    om = objectMapper
  }

  /**
   * UNF: superclass just checks response status code, I need to actually
   * process the response body to try to prevent some 500-status responses
   * from being converted into exceptions
   */
  @Override
  boolean hasError(ClientHttpResponse response) throws IOException {
    //String body = new String(getResponseBody(response))
    //log.error "error body: ${body}"

    super.hasError(response)
  }
}
