package edu.hawaii.its.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.Environment

import edu.hawaii.its.demo.fob.GroupService

import groovy.util.logging.Slf4j

/**
 * Application context root for grouper-demo application
 */
@Slf4j
@SpringBootApplication
class GrouperDemoApplication implements ApplicationRunner {
  @Value("\${test.enrolled.uhuuid}")
  String testEnrolledUhuuid

  @Value("\${test.informed.uhuuid}")
  String testInformedUhuuid

  @Value("\${test.irrelevant.uhuuid}")
  String irrelevantUhuuid

  @Value("\${test.nonexistent.uhuuid}")
  String nonexistentUhuuid

  @Autowired
  GroupService gs

  @Autowired
  Environment env

  static void main(String[] args) {
    SpringApplication.run(GrouperDemoApplication, args)
  }

  // UNF: need to actively investigate pooling, especially for the two-query version

  @Override
  void run(ApplicationArguments args) throws Exception {
    if (!env.activeProfiles.contains('testing')) {
      queryRoles()
      queryForms()

      try {
        queryInvalid()
      } catch (Exception e) {
        log.error('explicit exception thrown from queryInvalid', e)
      }
    }
  }

  void queryRoles() throws Exception {
    List<String> roles1 = gs.getRolesByUhuuid(testEnrolledUhuuid)
    log.error "roles(enrolled)=${roles1}"

    List<String> roles2 = gs.getRolesByUhuuid(irrelevantUhuuid)
    log.error "roles(irrelevant)=${roles2}"

    List<String> roles3 = gs.getRolesByUhuuid(nonexistentUhuuid)
    log.error "roles(nonexistent)=${roles3}"
  }

  void queryForms() throws Exception {
    List<String> forms1 = gs.getFormsByUhuuid(testInformedUhuuid)
    log.error "forms(informed)=${forms1}"

    List<String> forms2 = gs.getFormsByUhuuid(irrelevantUhuuid)
    log.error "forms(irrelevant)=${forms2}"

    List<String> forms3 = gs.getFormsByUhuuid(nonexistentUhuuid)
    log.error "forms(nonexistent)=${forms3}"
  }

  void queryInvalid() throws Exception {
    gs.getFormsByUhuuid(null)
  }
}
