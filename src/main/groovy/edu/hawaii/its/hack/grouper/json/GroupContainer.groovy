package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import groovy.transform.ToString

/**
 * json model class, used internally by {@link GroupContainerConverter}
 */
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class GroupContainer {
  @JsonProperty('wsGroups')
  List<Group> groups
}
