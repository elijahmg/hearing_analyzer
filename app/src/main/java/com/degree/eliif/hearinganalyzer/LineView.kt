package com.degree.eliif.hearinganalyzer

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class LineView(private val fq: Double, private val levelDbHl: Int) : Drawable() {
  /** 16 **/
  var frequincies = mutableListOf(
    125.0, 250.0, 500.0, 750.0,
    1000.0, 1500.0, 2000.0,
    3000.0, 4000.0, 8000.0,
    9000.0, 10000.0, 112000.0,
    14000.0, 16000.0)

  /** 19 **/
  var levels = mutableListOf(
    -10, -5, 0, 5, 10, 15, 20,
    25, 30, 35, 40, 45, 50, 55, 60, 65,
    70, 75, 80)

  val coordinatesMapXFq = mutableMapOf<Double, Float>()
  val coordinatesMapYLevel = mutableMapOf<Int, Float>()

  val initialCoordinatesFqLevel = mutableMapOf<Double, Int>()
  val initialCoordinatesXY = mutableMapOf<Float, Float>()

  init {
    for ((index, y) in (80..530 step 25).withIndex()) {
      coordinatesMapYLevel[levels[index]] = y.toFloat()
    }

    for ((index, x) in (110..970 step 60).withIndex()) {
      coordinatesMapXFq[frequincies[index]] = x.toFloat()
    }

    frequincies.forEach { t -> initialCoordinatesFqLevel[t] = 40 }

    initialCoordinatesFqLevel[fq] = levelDbHl

    initialCoordinatesFqLevel.map { (key, value) ->
      run {
        val x = coordinatesMapXFq[key]
        val y = coordinatesMapYLevel[value]

        if (y != null && x != null) {
          initialCoordinatesXY[x.toFloat()] = y.toFloat()
        }
      }
    }
  }

  override fun draw(canvas: Canvas) {

    this.drawGraph(canvas)

    initialCoordinatesXY.forEach { (x, y) ->
      this.drawCircle(canvas, x, y)
      this.drawCross(canvas, x, y)
    }
  }

  private fun drawCross(canvas: Canvas, x: Float, y: Float) {
    val paint = Paint()
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3F
    paint.color = Color.BLUE

    canvas.drawLine(x - 15F, y - 20F, x + 15f, y + 20F, paint)
    canvas.drawLine(x - 15F, y + 20F, x + 15f, y - 20F, paint)
  }

  private fun drawCircle(canvas: Canvas, cx: Float, cy: Float) {
    val paint = Paint()
    paint.color = Color.RED
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 3F

    canvas.drawCircle(cx, cy, 20F, paint)
  }

  private fun drawGraph(canvas: Canvas) {
    val paint = Paint()
    paint.color = Color.BLACK
    paint.strokeWidth = 4F
    paint.textSize = 30F

    val xVal = arrayListOf("125", "250", "500", "750", "1k", "1.5k", "2k", "3k", "4k", "8k", "9k", "10k", "11.2k", "14k", "16k")

    // Vertical
    for ((index, x) in (50..1020 step 60).withIndex()) {
      canvas.drawLine(x.toFloat(), 30F, x.toFloat(), 580F, paint)
      if (index != 0) {
        if (index - 1 < xVal.size) {
          canvas.drawText(xVal[index - 1], x.toFloat() - 20, 20F, paint)
        }
      }
    }

    val yVal = arrayListOf("-10", "0", "10", "20", "30", "40", "50", "60", "70", "80")

    // Horizontal
    for ((index, y) in (30..620 step 50).withIndex()) {
      canvas.drawLine(50F, y.toFloat(), 1010F, y.toFloat(), paint)

      // render y vals
      if (index != 0) {
        if (index - 1 < yVal.size) {
          canvas.drawText(yVal[index - 1], 0F, y.toFloat() + 10F, paint)
        }
      }
    }
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}