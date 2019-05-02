package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import groovy.transform.ToString

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class GroupContainer {
  @JsonProperty("wsGroups")
  List<Group> groups
}
