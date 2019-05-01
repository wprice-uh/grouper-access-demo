package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.web.client.RestTemplate

import edu.internet2.middleware.grouperClient.api.GcGetGroups
import edu.internet2.middleware.grouperClient.ws.StemScope
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResult
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResults
import edu.internet2.middleware.grouperClient.ws.beans.WsGroup
import edu.internet2.middleware.grouperClient.ws.beans.WsResultMeta
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j

@Slf4j
@SpringBootApplication
class HackApplication implements ApplicationRunner {
  @Value("\${test.admin.uhuuid}")
  String testAdminUhuuid

  @Value("\${grouper.url}")
  String grouperUrl

  @Value("\${grouper.username}")
  String grouperUsername

  @Value("\${grouper.password}")
  String grouperPassword

  @Value("\${grouper.search.root}")
  String grouperSearchRoot

  static void main(String[] args) {
    SpringApplication.run(HackApplication, args)
  }

  @Override
  void run(ApplicationArguments args) throws Exception {
    queryAllInSubtree()
    querySeparateSubtrees()
    queryAllInSubtree()
    querySeparateSubtrees()
  }

  @Bean
  RestTemplate grouperTemplate(RestTemplateBuilder builder) {
    builder
        .basicAuthentication(grouperUsername, grouperPassword)
        .build()
  }

  static String stemQuery(String stemName, boolean recurse) {
    // creating aliases for variables in the query
    def nameKey = 'stemName'
    def scopeKey = 'stemScope'
    def placeholder = 'placeholder'

    def stemEntry = [(nameKey):placeholder]
    def requestMap = [
        "subjectLookups": [
            ["subjectId": "11733562"],
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

  RequestEntity<String> stemEntity(String stemName, boolean recurse) {
    String queryJson = stemQuery(stemName, recurse)

    URI subjectUri = new URL("${grouperUrl}/v2_2_200/subjects").toURI()
    RequestEntity
        .post(subjectUri)
        .contentType(new MediaType('text', 'x-json'))
        .body(queryJson)
  }

  void querySeparateSubtrees() throws Exception {
    String rolesStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:admin'
    String formsStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:forms'

    RequestEntity<String> rolesEntity = stemEntity(rolesStem, false)
    RequestEntity<String> formsEntity = stemEntity(formsStem, false)

    RestTemplate t = grouperTemplate()
    def rolesResult = t.exchange(rolesEntity, String)
    def formsResult = t.exchange(formsEntity, String)
    log.error "raw results: \n-----\n${rolesResult}\n-----\n${formsResult}\n-----\n"
  }

  // UNF: need to intentionally create some errors and then write code to handle (converting) them
  // UNF: need to actively investigate pooling, especially for the two-query version

  void queryAllInSubtree() throws Exception {
    String allStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase'

    RequestEntity<String> allEntity = stemEntity(allStem, true)

    RestTemplate t = grouperTemplate()
    def allResult = t.exchange(allEntity, String)
    log.error "raw results: \n-----\n${allResult}\n-----\n"
  }

  void grouperClientQuery() {
    GcGetGroups gcGetGroups = new GcGetGroups()
        .addSubjectLookup(
            new WsSubjectLookup(testAdminUhuuid, null, null)
        )
        .assignWsStemLookup(
            new WsStemLookup(grouperSearchRoot, null)
        )
        .assignStemScope(StemScope.ALL_IN_SUBTREE)
        .assignEnabled(true)

    WsGetGroupsResults results = gcGetGroups.execute()

    WsResultMeta resultMetadata = results.getResultMetadata()
    if (resultMetadata.getSuccess() != 'T') {
      throw new RuntimeException("Error getting groups: " + resultMetadata.getSuccess()
          + ", " + resultMetadata.getResultCode()
          + ", " + resultMetadata.getResultMessage())
    }

    log.error "processing results"
    WsGetGroupsResult[] wsGroupsResults = results.getResults()
    for (WsGetGroupsResult result : wsGroupsResults) {
      for (WsGroup group : result.getWsGroups()) {
        log.error "group: (${group.name}, ${group.typeOfGroup}, ${group.displayExtension})"
      }
    }
  }
}
