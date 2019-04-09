package com.degree.eliif.hearinganalyzer.POJO

import java.io.Serializable

class Calibration : Serializable {
  var calibrationLeft: MutableMap<Double, Long> = mutableMapOf()
  var calibrationRight: MutableMap<Double, Long> = mutableMapOf()

  var dbHl: MutableMap<Double, Long> = mutableMapOf()
}