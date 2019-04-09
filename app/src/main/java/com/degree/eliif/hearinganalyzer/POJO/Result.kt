package com.degree.eliif.hearinganalyzer.POJO

import java.io.Serializable

class Result : Serializable {
  var resultsLeft: MutableMap<Double, Long> = mutableMapOf()
  var resultsRight: MutableMap<Double, Long> = mutableMapOf()
}