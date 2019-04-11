package com.degree.eliif.hearinganalyzer.POJO

import java.io.Serializable

class Calibration : Serializable {
  var calibrationLeft: MutableMap<Double, Double> = mutableMapOf()
  var calibrationRight: MutableMap<Double, Double> = mutableMapOf()

  var dbHl: MutableMap<Double, Double> = mutableMapOf()

  var measuringLevel: Double = 0.0

  override fun toString(): String {
    return calibrationLeft.toString() + calibrationLeft.toString() + dbHl.toString() + measuringLevel.toString()
  }
}