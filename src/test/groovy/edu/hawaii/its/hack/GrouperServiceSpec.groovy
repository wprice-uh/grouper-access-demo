package edu.hawaii.its.hack

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

import edu.hawaii.its.hack.grouper.GrouperService
import edu.hawaii.its.hack.grouper.json.GroupResults

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
@SpringBootTest
class GrouperServiceSpec extends Specification {
  @Value("\${test.enrolled.uhuuid}")
  String enrolledUhuuid

  @Value("\${test.informed.uhuuid}")
  String informedUhuuid

  @Value("\${test.irrelevant.uhuuid}")
  String irrelevantUhuuid

  @Value("\${test.nonexistent.uhuuid}")
  String nonexistentUhuuid

  @Value("\${fob.grouper.roles}")
  String fobRolesStem

  @Autowired
  GrouperService gs

  /* boundary conditions */

  @Test
  void 'invalid query'() {
    when:
    GroupResults errResult = gs.querySubtree(null, fobRolesStem)

    then:
    errResult
    // UNF: other assertions
  }
}
