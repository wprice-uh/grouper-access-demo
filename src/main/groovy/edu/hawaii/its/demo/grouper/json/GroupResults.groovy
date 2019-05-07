package edu.hawaii.its.demo.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

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
  GroupContainer groupContainer
}
