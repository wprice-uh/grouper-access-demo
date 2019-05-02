package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("WsGetGroupsResults")
class GetGroupsResults {
  ResultMetadata resultMetadata
  List<SubjectGroupResult> results
}
