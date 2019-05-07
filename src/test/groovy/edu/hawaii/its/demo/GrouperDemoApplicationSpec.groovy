package edu.hawaii.its.demo

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import groovy.util.logging.Slf4j
import spock.lang.Specification

@SpringBootTest
@Slf4j
class GrouperDemoApplicationSpec extends Specification {
  @Autowired
  GrouperDemoApplication application

  @Test
  void "testing spring initialization"() {
    log.info "entering \'${specificationContext.currentIteration.name}\'"

    expect:
    application != null

    log.info "exiting \'${specificationContext.currentIteration.name}\'"
  }
}
