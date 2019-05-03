ruleset {
  ruleset("file:config/codenarc/SharedConfig.groovy")

  // moved into MainConfig to allow overriding their defaults in TestConfig
  AbstractClassWithoutAbstractMethod
  ClassJavadoc
  CloneWithoutCloneable
  MethodCount
  MethodName
  MethodReturnTypeRequired
  MissingOverrideAnnotation
  VariableName
}
