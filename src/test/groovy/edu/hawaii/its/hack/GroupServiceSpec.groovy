package edu.hawaii.its.hack

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

import edu.hawaii.its.hack.fob.GroupService

import groovy.util.logging.Slf4j
import spock.lang.Ignore
import spock.lang.Specification

@Slf4j
@SpringBootTest
class GroupServiceSpec extends Specification {
  @Value("\${test.enrolled.uhuuid}")
  String enrolledUhuuid

  @Value("\${test.informed.uhuuid}")
  String informedUhuuid

  @Value("\${test.irrelevant.uhuuid}")
  String irrelevantUhuuid

  @Value("\${test.nonexistent.uhuuid}")
  String nonexistentUhuuid

  @Autowired
  GroupService gs

  /* roles */

  @Test
  void 'has roles'() {
    when:
    List<String> roles = gs.getRolesByUhuuid(enrolledUhuuid)

    then:
    roles == ['fob-admins', 'fob-developers']
  }

  @Test
  void 'no roles'() {
    when:
    List<String> roles = gs.getRolesByUhuuid(irrelevantUhuuid)

    then:
    roles == []
  }

  // UNF: need to make service stop treating nonexistent-user as an error
  @Test
  @Ignore
  void 'nonexistent user has no roles'() {
    when:
    List<String> roles = gs.getRolesByUhuuid(nonexistentUhuuid)

    then:
    roles == []
  }

  /* forms */

  @Test
  void 'has forms'() {
    when:
    List<String> forms = gs.getFormsByUhuuid(informedUhuuid)

    then:
    forms == ['fob-form-integration-testing']
  }

  @Test
  void 'no forms'() {
    when:
    List<String> forms = gs.getFormsByUhuuid(irrelevantUhuuid)

    then:
    forms == []
  }

  // UNF: need to make service stop treating nonexistent-user as an error
  @Test
  @Ignore
  void 'nonexistent user has no forms'() {
    when:
    List<String> forms = gs.getFormsByUhuuid(nonexistentUhuuid)

    then:
    forms == []
  }
}
