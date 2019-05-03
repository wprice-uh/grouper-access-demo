package edu.hawaii.its.hack

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.util.StopWatch

import edu.hawaii.its.hack.grouper.GrouperService
import edu.hawaii.its.hack.grouper.json.GroupResults

import groovy.util.logging.Slf4j

@Slf4j
@SpringBootApplication
class HackApplication implements ApplicationRunner {
  @Value("\${test.admin.uhuuid}")
  String testAdminUhuuid

  @Value("\${test.irrelevant.uhuuid}")
  String testExistentIrrelevantUhuuid

  static void main(String[] args) {
    SpringApplication.run(HackApplication, args)
  }

  // UNF: need to actively investigate pooling, especially for the two-query version

  // UNF: need to intentionally create some errors and then write code to handle (converting) them

  @Override
  void run(ApplicationArguments args) throws Exception {
    queryNonexistentUser()
  }

  void timingRun(ApplicationArguments args) throws Exception {
    doEverything()

    StopWatch allWatch = new StopWatch('queryAll')
    StopWatch sepWatch = new StopWatch('querySeparate')

    for (int i = 0; i < 10; ++i) {
      allWatch.start("queryAll ${i}")
      queryAllInSubtree()
      allWatch.stop()
    }

    for (int i = 0; i < 10; ++i) {
      sepWatch.start("querySeparate ${i}")
      querySeparateSubtrees()
      sepWatch.stop()
    }

    log.error allWatch.prettyPrint()
    log.error sepWatch.prettyPrint()
  }

  void doEverything() {
    queryAllInSubtree()
    querySeparateSubtrees()
    queryBlankResult()

    try {
      queryNonexistentUser()
    } catch (Exception e) {
      log.error("exception for nonexistent", e)
    }

    try {
      queryInvalid()
    } catch (Exception e) {
      log.error("exception for invalid", e)
    }
  }

  @Autowired
  GrouperService grouperService

  void queryRoles() throws Exception {
    String rolesStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:roles'
    GroupResults rolesResult = grouperService.querySubtree(testAdminUhuuid, rolesStem, false)
    log.error "raw roles result: ${rolesResult}"
    log.error "raw roles dump: ${rolesResult.dump()}"
  }

  void querySeparateSubtrees() throws Exception {
    String rolesStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:roles'
    String formsStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase:forms'

    GroupResults rolesResult = grouperService.querySubtree(testAdminUhuuid, rolesStem, false)
    log.error "raw roles result: ${rolesResult}"
    log.error "raw roles dump: ${rolesResult.dump()}"

    GroupResults formsResult = grouperService.querySubtree(testAdminUhuuid, formsStem, false)
    log.error "raw forms result: ${formsResult}"
    log.error "raw forms dump: ${formsResult.dump()}"
  }

  void queryAllInSubtree() throws Exception {
    String allStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase'

    def allResult = grouperService.querySubtree(testAdminUhuuid, allStem, true)
    log.error "raw all results: \n-----\n${allResult}\n-----\n"
  }

  void queryBlankResult() throws Exception {
    String allStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase'

    def blankResult = grouperService.querySubtree(testExistentIrrelevantUhuuid, allStem, true)
    log.error "raw blank results: \n-----\n${blankResult}\n-----\n"
  }

  void queryNonexistentUser() throws Exception {
    String allStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase'
    String nonexistentUhuuid = '00000000'

    def blankResult = grouperService.querySubtree(nonexistentUhuuid, allStem, true)
    log.error "raw blank results: \n-----\n${blankResult}\n-----\n"
  }

  void queryInvalid() throws Exception {
    String allStem = 'hawaii.edu:custom:uhsystem:its:mis:forms-for-onbase'

    String errResult = grouperService.querySubtree(null, allStem, true)
    log.error "raw error results: \n-----\n${errResult}\n-----\n"
  }
}
