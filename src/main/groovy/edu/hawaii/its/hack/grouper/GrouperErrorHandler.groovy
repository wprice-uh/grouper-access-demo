package edu.hawaii.its.hack.grouper

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler

import groovy.util.logging.Slf4j

@Slf4j
class GrouperErrorHandler extends DefaultResponseErrorHandler {
  @Override
  protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
    String rbody = new String(getResponseBody(response))
    log.error "error body: ${rbody}"

    super.handleError(response, statusCode)
  }
}
