package com.degree.eliif.hearinganalyzer

import java.io.Serializable

class Result : Serializable {
  var name: String = ""
  var results: Array<String> = Array(17) { "" } // @todo change to map frequency : value, !!!side

  var goodResults = arrayOf(50, 90) // todo
}