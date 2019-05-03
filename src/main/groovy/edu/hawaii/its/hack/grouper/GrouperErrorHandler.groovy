package edu.hawaii.its.hack.grouper

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import com.fasterxml.jackson.databind.ObjectMapper

import edu.hawaii.its.hack.grouper.json.GroupResults

import groovy.util.logging.Slf4j

@Slf4j
class GrouperErrorHandler extends DefaultResponseErrorHandler {
  final ObjectMapper om

  GrouperErrorHandler(ObjectMapper objectMapper) {
    om = objectMapper
  }

  @Override
  protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
    String rawBody = new String(getResponseBody(response))
    log.error "raw error: ${rawBody}"

    try {
      GroupResults results = om.readValue(rawBody, GroupResults)
      log.error "extracted error: ${results}"
    } catch (Exception e) {
      log.error("exception extracting rest error details", e)
      super.handleError(response, statusCode)
    }
  }
}
