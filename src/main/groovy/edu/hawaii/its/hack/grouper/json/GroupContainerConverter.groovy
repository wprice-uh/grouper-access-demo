package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.databind.util.StdConverter

/**
 * Converts GroupContainer instances, created during json binding, into the
 * simple List<Group> which the client ultimately receives
 */
class GroupContainerConverter extends StdConverter<GroupContainer, List<Group>> {
  @Override
  List<Group> convert(GroupContainer value) {
    value.groups
  }
}
