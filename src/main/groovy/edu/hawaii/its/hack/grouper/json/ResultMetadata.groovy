package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import groovy.transform.ToString

/**
 * json model class, describing results of rest invocation
 */
@ToString
class ResultMetadata {
  String resultCode

  @JsonProperty(required = false)
  String resultMessage

  @JsonDeserialize(converter = StringBooleanConverter)
  boolean success
}
