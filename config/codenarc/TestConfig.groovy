ruleset {
  ruleset("file:config/codenarc/SharedConfig.groovy")

  /*
   * these enhanced rules break, with no output indicating the proble, when
   * tests inherit from classes in the same source tree (i.e. in one instance,
   * moving the class out into 'main' made the failures disappear)
   */
  JUnitAssertEqualsConstantActualValue(enabled: false)
  CloneWithoutCloneable(enabled: false)
  MissingOverrideAnnotation(enabled: false)

  /* these JUnit-related rules should only be applied to tests */
  ChainedTest
  CoupledTestCase
  JUnitAssertAlwaysFails
  JUnitAssertAlwaysSucceeds
  JUnitFailWithoutMessage
  JUnitLostTest
  JUnitPublicField
  JUnitPublicNonTestMethod
  JUnitSetUpCallsSuper
  JUnitStyleAssertions
  JUnitTearDownCallsSuper
  JUnitUnnecessarySetUp
  JUnitUnnecessaryTearDown
  JUnitUnnecessaryThrowsException
  SpockIgnoreRestUsed
  UnnecessaryFail
  UseAssertEqualsInsteadOfAssertTrue
  UseAssertFalseInsteadOfNegation
  UseAssertNullInsteadOfAssertEquals
  UseAssertSameInsteadOfAssertTrue
  UseAssertTrueInsteadOfAssertEquals
  UseAssertTrueInsteadOfNegation

  // method names in specs allow most punctuation marks
  MethodName { regex = "[0-9a-z][-+., ()#_\\w]*" }

  // spec classes aren't allowed to have constructors, so making spec base
  // classes abstract is the only way to prevent their instantiation
  AbstractClassWithoutAbstractMethod { doNotApplyToClassNames = '*SpecBase' }

  // spec classes are self-explanatory
  ClassJavadoc { doNotApplyToFileNames = "*Spec.groovy" }

  // disagrees with groovy's default behavior for properties
  JUnitPublicProperty(enabled: false)

  // generates false positives for specs
  JUnitTestMethodWithoutAssert { doNotApplyToClassNames = '*Spec' }

  // test methods are allowed to proliferate
  MethodCount(enabled: false)

  // test methods are allowed to have dynamic return types
  MethodReturnTypeRequired { doNotApplyToClassNames = '*Spec' }

  // test methods are allowed to have single-character lowercased variable names
  VariableName { finalRegex = "[a-z][a-zA-Z0-9]*" }
}
