package edu.hawaii.its.demo.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import groovy.transform.ToString

/**
 * json model class
 */
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class GroupContainer {
  @JsonProperty('resultMetadata')
  ResultMetadata metadata

  @JsonProperty('wsGroups')
  List<Group> groups
}
