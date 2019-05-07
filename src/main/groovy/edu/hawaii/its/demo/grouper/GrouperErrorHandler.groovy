package edu.hawaii.its.demo.grouper

import java.nio.charset.Charset
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.HttpServerErrorException
import com.fasterxml.jackson.databind.ObjectMapper

import edu.hawaii.its.demo.grouper.json.GroupResults

import groovy.util.logging.Slf4j

/**
 * Augments default error processing to convert some well-formed errors
 * returned from the server into non-exceptions, and to provide better
 * exception messages for others
 */
@Slf4j
class GrouperErrorHandler extends DefaultResponseErrorHandler {
  final ObjectMapper om
  final Pattern restErrorPattern
  GrouperErrorHandler(ObjectMapper objectMapper) {
    om = objectMapper

    String regex = /.*WsSubjectLookup\[.*subjectFindResult=(.*)].*/
    restErrorPattern = Pattern.compile(regex, Pattern.DOTALL)
  }

  /**
   * Prevent some 500 errors from being treated as exceptions
   */
  @Override
  boolean hasError(ClientHttpResponse response) throws IOException {
    super.hasError(response) && !ignorableError(response)
  }

  /**
   * @return true if the error is one which should be ignored (i.e. which
   * can be better handled by converting the response rather than throwing
   * an exception)
   */
  boolean ignorableError(ClientHttpResponse response) {
    boolean ignorable = false

    int rawStatusCode = response.getRawStatusCode()
    HttpStatus statusCode = HttpStatus.resolve(rawStatusCode)

    String errorMessage = retrieveErrorMessage(response, statusCode)
    if (errorMessage) {
      ignorable = errorMessage.contains('SUBJECT_NOT_FOUND')
    }

    ignorable
  }

  /**
   * @param response
   * @return most-specific, possibly-nested error-related status message from
   * the given response
   */
  String retrieveErrorMessage(ClientHttpResponse response, HttpStatus statusCode) {
    String statusMessage = null

    // retrieve the most-specific error message available, if any
    if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
      byte[] rawBody = getResponseBody(response)
      GroupResults parsed = parseBody(rawBody)

      if (parsed) {
        // use the inner message, where available
        if (parsed.groupContainer && !parsed.groupContainer.metadata.success) {
          statusMessage = parsed.groupContainer.metadata.resultMessage
        } else if (!parsed.metadata.success) {
          statusMessage = parsed.metadata.resultMessage
        }
      }
    }

    // replace with even more specific details, if available
    if (statusMessage) {
      Matcher errorMatcher = restErrorPattern.matcher(statusMessage)
      if (errorMatcher.matches()) {
        log.debug "errorMatcher match 1: ${errorMatcher.group(1)}"
        statusMessage = errorMatcher.group(1)
      }
    }

    statusMessage
  }

  @Override
  protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
    boolean handled = false

    if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
      byte[] rawBody = getResponseBody(response)
      GroupResults parsed = parseBody(rawBody)

      // use parseable error messages to create more readable exceptions
      if (parsed) {
        if (!parsed.metadata.success) {
          String serverMessage = retrieveErrorMessage(response, statusCode)
          Charset charset = getCharset(response)

          log.error "throwing 500 error: ${serverMessage}"
          throw HttpServerErrorException.create(
              statusCode, serverMessage, response.headers, rawBody, charset
          )
        }
      }
    }

    // default to superclass handling, if needed
    if (!handled) {
      super.handleError(response, statusCode)
    }
  }

  /**
   * @param rawBody
   * @return GroupResults if response body is parseable, otherwise null
   */
  GroupResults parseBody(byte[] rawBody) {
    GroupResults result = null

    try {
      result = om.readValue(rawBody, GroupResults)
    } catch (Exception ignored) {
    }

    result
  }
}
