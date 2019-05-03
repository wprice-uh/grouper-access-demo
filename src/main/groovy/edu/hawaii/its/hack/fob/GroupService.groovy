package edu.hawaii.its.hack.fob

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import edu.hawaii.its.hack.grouper.GrouperService
import edu.hawaii.its.hack.grouper.json.GroupResults

@Component
class GroupService {
  @Value("\${fob.grouper.base}")
  String fobBaseStem

  @Value("\${fob.grouper.roles}")
  String fobRolesStem

  @Value("\${fob.grouper.forms}")
  String fobFormsStem

  @Autowired
  GrouperService grouperService

  List<String> getRolesByUhuuid(String uhuuid) {
    GroupResults result = grouperService.querySubtree(uhuuid, fobRolesStem)
    result.groups*.extension ?: []
  }

  List<String> getFormsByUhuuid(String uhuuid) {
    GroupResults result = grouperService.querySubtree(uhuuid, fobFormsStem)
    result.groups*.extension ?: []
  }
}
