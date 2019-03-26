package com.degree.eliif.hearinganalyzer

import java.io.Serializable

class Result : Serializable {
  var name: String = "" // todo
  var resultsLeft: MutableMap<Double, Long> = mutableMapOf()
  var resultsRight: MutableMap<Double, Long> = mutableMapOf()

  var goodResults: MutableMap<Double, Long> = mutableMapOf()

  init {
    val frequencyArray = arrayOf(125, 250, 500, 750, 1000, 1500, 2000, 3000, 4000, 6000, 8000, 9000, 10000, 11200, 14000, 16000, 20000)
    frequencyArray.forEach { goodResults[it.toDouble()] = -30 }
  }

}