// uncomment next line to debug logback itself
// statusListener(OnConsoleStatusListener)

String logDir = 'target/logs'
String logName = 'grouper-access-demo'

String layoutPattern = '%date %5level %property{PID} --- [%15.15thread] %logger : %m%n'

appender('LOGFILE', FileAppender) {
  encoder(PatternLayoutEncoder) { pattern = layoutPattern }

  file = "${logDir}/${logName}.log"
}

appender('CONSOLE', ConsoleAppender) {
  encoder(PatternLayoutEncoder) { pattern = layoutPattern }
}

// configure appenders
//   everything defaults to INFO
root(INFO, ['CONSOLE', 'LOGFILE'])

// override log levels
logger('edu.hawaii', DEBUG)
logger('org.springframework.web', DEBUG)

// enable debugging for web components
logger('web', DEBUG)

// disable tomcat noise
logger('org.apache.catalina.core.AprLifecycleListener', WARN)
