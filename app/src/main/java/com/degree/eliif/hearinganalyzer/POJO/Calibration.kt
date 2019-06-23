package com.degree.eliif.hearinganalyzer.POJO

import java.io.Serializable

class Calibration : Serializable {
  var calibrationLeft: MutableMap<Double, Float> = mutableMapOf()
  var calibrationRight: MutableMap<Double, Float> = mutableMapOf()

  var dbHl: MutableMap<Double, Float> = mutableMapOf()

  var measuringLevel: Float = 0.0F
}