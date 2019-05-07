package edu.hawaii.its.demo

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.HttpServerErrorException

import edu.hawaii.its.demo.grouper.GrouperService

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

  /* exploring boundary conditions */

  @Test
  void 'invalid query'() {
    when:
    gs.querySubtree(null, fobRolesStem)

    then:
    HttpServerErrorException e = thrown()

    and:
    e.message.contains('INVALID_QUERY')
  }
}
