package com.degree.eliif.hearinganalyzer

import java.io.Serializable

class Result : Serializable {
  var name: String = "" // todo
  lateinit var resultsLeft: MutableMap<Double, Long>
  lateinit var resultsRight: MutableMap<Double, Long>

  var goodResults = arrayOf(50, 90) // todo
}