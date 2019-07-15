package com.degree.eliif.hearinganalyzer.POJO

import java.io.Serializable

class Coordination: Serializable {
  var frequincies = mutableListOf(
    125.0, 250.0, 500.0, 750.0,
    1000.0, 1500.0, 2000.0,
    3000.0, 4000.0, 8000.0,
    9000.0, 10000.0, 11200.0,
    14000.0, 16000.0)

  /** 21 **/
  private var levels = mutableListOf(
    -10, -5, 0, 5, 10, 15, 20,
    25, 30, 35, 40, 45, 50, 55, 60, 65,
    70, 75, 80, 85, 90, 95)

  private val coordinatesMapXFq = mutableMapOf<Double, Float>()
  private val coordinatesMapYLevel = mutableMapOf<Int, Float>()

  private val leftCoordinatesFqLevel = mutableMapOf<Double, Int>()
  private val rightCoordinatesFqLevel = mutableMapOf<Double, Int>()
  val leftCoordinatesXY = mutableMapOf<Float, Float>()
  val rightCoordinatesXY = mutableMapOf<Float, Float>()

  init {
    for ((index, y) in (170..820 step 30).withIndex()) {
      coordinatesMapYLevel[levels[index]] = y.toFloat()
    }

    for ((index, x) in (180..1160 step 70).withIndex()) {
      coordinatesMapXFq[frequincies[index]] = x.toFloat()
    }

    frequincies.forEach { t ->
      run {
        leftCoordinatesFqLevel[t] = 40
        rightCoordinatesFqLevel[t] = 40
      }
    }

    leftCoordinatesFqLevel.map { (key, value) ->
      run {
        val x = coordinatesMapXFq[key]
        val y = coordinatesMapYLevel[value]

        if (y != null && x != null) {
          leftCoordinatesXY[x.toFloat()] = y.toFloat()
          rightCoordinatesXY[x.toFloat()] = y.toFloat()
        }
      }
    }
  }

  fun changeCoordinates(frequency: Double = 125.0, level: Int = 40, leftSide: Boolean = true) {
    if (leftSide) {
      this.changeLeftCoordination(frequency, level)
    } else {
      this.changeRightCoordination(frequency, level)
    }
  }

  private fun changeLeftCoordination(fq: Double, level: Int) {
    leftCoordinatesFqLevel[fq] = level

    val x = coordinatesMapXFq[fq]
    val y = coordinatesMapYLevel[level]

    if (y != null && x != null) {
      leftCoordinatesXY[x.toFloat()] = y.toFloat()
    }
  }

  private fun changeRightCoordination(fq: Double, level: Int) {
    rightCoordinatesFqLevel[fq] = level

    val x = coordinatesMapXFq[fq]
    val y = coordinatesMapYLevel[level]

    if (y != null && x != null) {
      rightCoordinatesXY[x.toFloat()] = y.toFloat()
    }
  }
}