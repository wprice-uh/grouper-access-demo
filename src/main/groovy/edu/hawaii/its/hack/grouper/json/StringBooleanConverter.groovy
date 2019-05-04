package edu.hawaii.its.hack.grouper.json

import com.fasterxml.jackson.databind.util.StdConverter

/**
 * Converts strings used to represent boolean values into Booleans.
 */
class StringBooleanConverter extends StdConverter<String, Boolean> {
  /**
   * Converts anything other than 'T' into Boolean.FALSE
   */
  @Override
  Boolean convert(String value) {
    (value && (value == 'T'))
  }
}
