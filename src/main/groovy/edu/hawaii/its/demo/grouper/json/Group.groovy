package edu.hawaii.its.demo.grouper.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import groovy.transform.ToString

/**
 * json model class, represents an individual group-membership
 */
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class Group {
  String description
  String extension
  String name
}
