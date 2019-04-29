package com.degree.eliif.hearinganalyzer.Functionality

import com.degree.eliif.hearinganalyzer.POJO.Calibration

class Computate(private val calibration: Calibration) {
  /**
   * Get level in dB SPL
   */
  fun getDbSpl(frequency: Double, leftSide: Boolean): Double? {
    return when (leftSide) {
      true -> calibration.calibrationLeft[frequency]
      false -> calibration.calibrationRight[frequency]
    }
  }

  /**
   * Get level in dB HL
   */
  fun getDbHl(frequency: Double, leftSide: Boolean): Double? {
    return this.getDbSpl(frequency, leftSide)!! - calibration.dbHl[frequency]!!
  }

  /**
   * Get level in float
   */
  fun getFloatLevelForNullSpl(frequency: Double, leftSide: Boolean): Double? {
    val calibrationFloatLevel = calibration.measuringLevel
    val measuredDbHLLevel = this.getDbHl(frequency, leftSide)
    return calibrationFloatLevel / (Math.pow(10.toDouble(), (measuredDbHLLevel!! / 20)))
  }
}