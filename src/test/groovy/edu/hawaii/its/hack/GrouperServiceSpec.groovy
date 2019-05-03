package edu.hawaii.its.hack

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import edu.hawaii.its.hack.grouper.GrouperService

import groovy.util.logging.Slf4j
import spock.lang.Specification

@SpringBootTest
@Slf4j
class GrouperServiceSpec extends Specification {
  @Autowired
  GrouperService gs

  @Test
  void "testing spring initialization"() {
    log.info "entering \'${specificationContext.currentIteration.name}\'"

    expect:
    gs != null

    log.info "exiting \'${specificationContext.currentIteration.name}\'"
  }
}
