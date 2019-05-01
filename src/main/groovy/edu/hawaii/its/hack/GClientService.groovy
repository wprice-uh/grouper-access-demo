package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import edu.internet2.middleware.grouperClient.api.GcGetGroups
import edu.internet2.middleware.grouperClient.ws.StemScope
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResult
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResults
import edu.internet2.middleware.grouperClient.ws.beans.WsGroup
import edu.internet2.middleware.grouperClient.ws.beans.WsResultMeta
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup

@Service
class GClientService {
  @Value("\${grouper.username}")
  String grouperUsername

  @Value("\${grouper.password}")
  String grouperPassword

  @Value("\${grouper.search.root}")
  String grouperSearchRoot


  void grouperClientQuery(String uhuuid) {
    GcGetGroups gcGetGroups = new GcGetGroups()
        .addSubjectLookup(
            new WsSubjectLookup(uhuuid, null, null)
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
