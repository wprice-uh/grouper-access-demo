package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import groovy.transform.ToString

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class Group {
  String description
  String extension
  String name
}
