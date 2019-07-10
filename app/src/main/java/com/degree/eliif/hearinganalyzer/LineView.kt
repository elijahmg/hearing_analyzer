package com.degree.eliif.hearinganalyzer

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class LineView(
  private val leftCoordinatesXY: MutableMap<Float, Float>,
  private val rightCoordinatesXY: MutableMap<Float, Float>) : Drawable() {

  override fun draw(canvas: Canvas) {

    this.drawGraph(canvas)
    this.drawLeft(canvas)
    this.drawRight(canvas)
  }

  private fun drawLeft(canvas: Canvas) {
    val path = Path()

    val paint = Paint()
    paint.color = Color.RED
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 6f

    val firstX = leftCoordinatesXY.keys.iterator().next()
    val firstY = leftCoordinatesXY[firstX]!!

    path.reset()

    path.moveTo(firstX, firstY)
    leftCoordinatesXY.forEach { (x, y) ->
      this.drawCircle(canvas, x, y)
      path.lineTo(x, y)
    }

    canvas.drawPath(path, paint)
  }

  private fun drawRight(canvas: Canvas) {
    val path = Path()

    val paint = Paint()
    paint.color = Color.BLUE
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 6f

    val firstX = leftCoordinatesXY.keys.iterator().next()
    val firstY = rightCoordinatesXY[firstX]!!

    path.reset()

    path.moveTo(firstX, firstY)
    rightCoordinatesXY.forEach { (x, y) ->
      this.drawCross(canvas, x, y)
      path.lineTo(x, y)
    }

    canvas.drawPath(path, paint)
  }

  private fun drawCross(canvas: Canvas, x: Float, y: Float) {
    val paint = Paint()
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 4F
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
    for ((index, x) in (150..1120 step 60).withIndex()) {
      if (index != 0 && index % 2 == 0 && x < 1080) {
        canvas.drawLine(x.toFloat(), 160F, x.toFloat(), 660F, paint)
      } else {
        canvas.drawLine(x.toFloat(), 110F, x.toFloat(), 660F, paint)
      }
      if (index != 0) {
        if (index - 1 < xVal.size) {
          if (index != 0 && index % 2 == 0 && x < 1080) {
            canvas.drawText(xVal[index - 1], x.toFloat() - 20, 150F, paint)

          } else {
            canvas.drawText(xVal[index - 1], x.toFloat() - 20, 100F, paint)
          }
        }
      }
    }

    val yVal = arrayListOf("-10", "0", "10", "20", "30", "40", "50", "60", "70", "80")

    // Horizontal
    for ((index, y) in (110..700 step 50).withIndex()) {
      canvas.drawLine(150F, y.toFloat(), 1110F, y.toFloat(), paint)

      // render y vals
      if (index != 0) {
        if (index - 1 < yVal.size) {
          canvas.drawText(yVal[index - 1], 100F, y.toFloat() + 10F, paint)
        }
      }
    }
    canvas.drawText("Frequencies [Hz]", 500F, 40F, paint)

    canvas.rotate(270F)
    canvas.drawText("dB [HL]", -450F, 80F, paint)
    canvas.rotate(-270F)
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}