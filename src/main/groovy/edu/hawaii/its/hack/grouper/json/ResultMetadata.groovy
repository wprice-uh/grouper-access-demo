package edu.hawaii.its.hack.grouper.json

import groovy.transform.ToString

/**
 * json model class, describing results of rest invocation
 */
@ToString
class ResultMetadata {
  String resultCode
  String resultMessage
  String success
}
