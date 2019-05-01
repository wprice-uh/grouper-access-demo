package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class GrouperResult {
  String responseMetadata
  String resultMetadata
  String result
}
