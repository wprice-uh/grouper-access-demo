package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import edu.hawaii.its.hack.fob.GroupService

import groovy.util.logging.Slf4j

@Slf4j
@SpringBootApplication
class HackApplication implements ApplicationRunner {
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

  static void main(String[] args) {
    SpringApplication.run(HackApplication, args)
  }

  // UNF: need to actively investigate pooling, especially for the two-query version

  // UNF: need to intentionally create some errors and then write code to handle (converting) them

  @Override
  void run(ApplicationArguments args) throws Exception {
    queryRoles()
    queryForms()
  }

  void queryRoles() throws Exception {
    List<String> roles1 = gs.getRolesByUhuuid(testEnrolledUhuuid)
    log.error "roles(enrolled)=${roles1}"

    List<String> roles2 = gs.getRolesByUhuuid(irrelevantUhuuid)
    log.error "roles(irrelevant)=${roles2}"

    // UNF: List<String> roles3 = gs.getRolesByUhuuid(nonexistentUhuuid)
    // log.error "roles(nonexistent)=${roles3}"
  }

  void queryForms() throws Exception {
    List<String> forms1 = gs.getFormsByUhuuid(testInformedUhuuid)
    log.error "forms(informed)=${forms1}"

    List<String> forms2 = gs.getFormsByUhuuid(irrelevantUhuuid)
    log.error "forms(irrelevant)=${forms2}"

    // UNF: List<String> forms3 = gs.getFormsByUhuuid(nonexistentUhuuid)
    // log.error "forms(nonexistent)=${forms3}"
  }
}
