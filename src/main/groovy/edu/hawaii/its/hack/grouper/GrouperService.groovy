package edu.hawaii.its.hack.grouper

import java.nio.charset.StandardCharsets

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import edu.hawaii.its.hack.grouper.json.GetGroupsResults

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

@Slf4j
@Service
class GrouperService {
  @Value("\${grouper.url}")
  String grouperUrl

  final RestTemplate grouperTemplate

  GrouperService(RestTemplate grouperTemplate) {
    this.grouperTemplate = grouperTemplate
  }

  static String stemQuery(String uhuuid, String stemName, boolean recurse) {
    // creating aliases for variables in the query
    def nameKey = 'stemName'
    def scopeKey = 'stemScope'
    def placeholder = 'placeholder'

    def stemEntry = [(nameKey): placeholder]
    def requestMap = [
        "subjectLookups": [
            ["subjectId": uhuuid],
        ],
        "wsStemLookup": stemEntry,
        (scopeKey): placeholder,
        "enabled": "T",
    ]
    def queryMap = [
        "WsRestGetGroupsRequest": requestMap
    ]

    stemEntry[nameKey] = stemName
    requestMap[scopeKey] = recurse ? 'ALL_IN_SUBTREE' : 'ONE_LEVEL'
    JsonOutput.toJson(queryMap)
  }

  RequestEntity<String> stemEntity(String uhuuid, String stemName, boolean recurse) {
    String queryJson = stemQuery(uhuuid, stemName, recurse)

    URI subjectUri = new URL("${grouperUrl}/v2_2_200/subjects").toURI()

    RequestEntity
        .post(subjectUri)
        .contentType(new MediaType('text', 'x-json', StandardCharsets.UTF_8))
        .body(queryJson)
  }

  GetGroupsResults querySubtree(String uhuuid, String stem, boolean recurse) {
    RequestEntity<String> rolesEntity = stemEntity(uhuuid, stem, recurse)

    ResponseEntity<String> response = grouperTemplate.exchange(rolesEntity, String)
    String rawBody = response.getBody()

    ObjectMapper om = new ObjectMapper()
    om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    om.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
    om.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true)

    om.readValue(rawBody, GetGroupsResults)
  }
}

