package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import groovy.util.logging.Slf4j

@Slf4j
@SpringBootApplication
class HackApplication implements ApplicationRunner {
  @Value("\${test.admin.uhuuid}")
  String testAdminUhuuid

  static void main(String[] args) {
    SpringApplication.run(HackApplication, args)
  }

  @Override
  void run(ApplicationArguments args) throws Exception {
    queryAllInSubtree()
    querySeparateSubtrees()
    queryAllInSubtree()
    querySeparateSubtrees()
  }

  @Autowired
  GrouperService grouperService

  void querySeparateSubtrees() throws Exception {
    String rolesStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:admin'
    String formsStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:forms'

    def rolesResult = grouperService.querySubtree(testAdminUhuuid, rolesStem, false)
    def formsResult = grouperService.querySubtree(testAdminUhuuid, formsStem, false)
    log.error "raw sep results: \n-----\n${rolesResult}\n-----\n${formsResult}\n-----\n"
  }

  // UNF: need to intentionally create some errors and then write code to handle (converting) them
  // UNF: need to actively investigate pooling, especially for the two-query version

  void queryAllInSubtree() throws Exception {
    String allStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase'

    def allResult = grouperService.querySubtree(testAdminUhuuid, allStem, true)
    log.error "raw all results: \n-----\n${allResult}\n-----\n"
  }

}
