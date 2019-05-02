package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.databind.util.StdConverter

class GroupContainerConverter extends StdConverter<GroupContainer, List<Group>> {
  @Override
  List<Group> convert(GroupContainer value) {
    value.groups
  }
}
