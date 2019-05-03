package edu.hawaii.its.hack.grouper

import java.nio.charset.StandardCharsets

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import edu.hawaii.its.hack.grouper.json.GroupResults

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

/**
 * Invokes grouper rest API, converts the results into simplified model
 * classes suitable for higher-level use
 */
@Slf4j
@Service
class GrouperService {
  @Value("\${grouper.url}")
  String grouperUrl

  final RestTemplate grouperTemplate

  GrouperService(RestTemplate grouperTemplate) {
    this.grouperTemplate = grouperTemplate
  }

  /**
   * Returns a GroupResults object containing a list of the groups
   * directly under the given stem to which the given user belongs
   *
   * @param uhuuid
   * @param stem
   * @return GroupResults
   */
  GroupResults querySubtree(String uhuuid, String stem) {
    RequestEntity<String> rolesEntity = stemEntity(uhuuid, stem)

    ResponseEntity<GroupResults> response = grouperTemplate.exchange(rolesEntity, GroupResults)
    response.body
  }

  /**
   * @param uhuuid
   * @param stemName
   * @return RequestEntity for invoking grouper's subject-lookup API
   */
  private RequestEntity<String> stemEntity(String uhuuid, String stemName) {
    String queryJson = stemQuery(uhuuid, stemName, false)

    URI subjectUri = new URL("${grouperUrl}/subjects").toURI()

    RequestEntity
        .post(subjectUri)
        .contentType(new MediaType('text', 'x-json', StandardCharsets.UTF_8))
        .body(queryJson)
  }

  /**
   * @param uhuuid
   * @param stemName
   * @param recurse
   * @return json-formatted query for input to grouper's subject-lookup API
   */
  private static String stemQuery(String uhuuid, String stemName, boolean recurse) {
    // creating aliases for variables in the query
    def nameKey = 'stemName'
    def scopeKey = 'stemScope'
    def placeholder = 'placeholder'

    def stemEntry = [(nameKey): placeholder]
    def requestMap = [
        'subjectLookups': [
            ['subjectId': uhuuid],
        ],
        'wsStemLookup': stemEntry,
        (scopeKey): placeholder,
        'enabled': 'T',
    ]
    def queryMap = [
        'WsRestGetGroupsRequest': requestMap
    ]

    stemEntry[nameKey] = stemName
    requestMap[scopeKey] = recurse ? 'ALL_IN_SUBTREE' : 'ONE_LEVEL'
    JsonOutput.toJson(queryMap)
  }
}

