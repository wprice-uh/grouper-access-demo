package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonProperty

class SubjectGroupResult {
  ResultMetadata resultMetadata

  @JsonProperty("wsGroups")
  List<GroupResult> groups

  @JsonProperty("wsSubject")
  SubjectResult subject
}
