package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import groovy.transform.ToString

/**
 * json model class, root of the tree returned by invoking grouper's
 * getGroups rest api
 */
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName('WsGetGroupsResults')
class GroupResults {
  @JsonProperty('resultMetadata')
  ResultMetadata metadata

  @JsonProperty('results')
  @JsonDeserialize(converter = GroupContainerConverter)
  List<Group> groups
}
