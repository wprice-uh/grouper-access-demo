package edu.hawaii.its.hack.grouper

import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler

import groovy.util.logging.Slf4j

/**
 * Parses all rest responses, to prevent things like "uhuuid unknown" from
 * throwing an exception instead of returning an empty group-membership
 * list
 */
@Slf4j
class GrouperErrorHandler extends DefaultResponseErrorHandler {
  /**
   * UNF: parse the response body, deescalate some 500s
   *
   * @return true if the given response should be converted into an exception
   */
  @Override
  boolean hasError(ClientHttpResponse response) throws IOException {
    // UNF: getResponseBody commits the stream, which breaks further
    // restTemplate attempts to process it... probably need to wrap
    // getResponseBody with something which checks for status 500, at least
    //
    //String body = new String(getResponseBody(response))
    //log.error "error body: ${body}"

    super.hasError(response)
  }
}
