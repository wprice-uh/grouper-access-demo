package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import edu.internet2.middleware.grouperClient.api.GcGetGroups
import edu.internet2.middleware.grouperClient.ws.StemScope
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResult
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResults
import edu.internet2.middleware.grouperClient.ws.beans.WsGroup
import edu.internet2.middleware.grouperClient.ws.beans.WsResultMeta
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup
import groovy.util.logging.Slf4j

@Slf4j
@SpringBootApplication
class HackApplication implements ApplicationRunner {
  @Value("\${test.admin.uhuuid}")
  String testAdminUhuuid

  static void main(String[] args) {
    SpringApplication.run(HackApplication, args)
  }

  @Override
  void run(ApplicationArguments args) throws Exception {
    thirdTry()
  }

  void thirdTry() {
    GcGetGroups gcGetGroups = new GcGetGroups()
        .addSubjectLookup(
            new WsSubjectLookup(testAdminUhuuid, null, null)
        )
        .assignWsStemLookup(
            new WsStemLookup('hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase', null)
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

  void firstTry() {
    try {
      log.error("---------------- pre-grouper  ----------------------")

      WsGetGroupsResults wsGetGroupsResults = new GcGetGroups()
          .addSubjectIdentifier(testAdminUhuuid)
          .assignStemScope(StemScope.ONE_LEVEL)
          .assignWsStemLookup(
              new WsStemLookup('hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase', null)
          )
          .assignIncludeSubjectDetail(Boolean.FALSE)
          .execute()
      log.error("groups: ${wsGetGroupsResults}")
      log.error("---------------- post-grouper ----------------------")
    }

    catch (
        Exception e
        ) {
      log.error("grouper exception", e)
    }
  }
}
