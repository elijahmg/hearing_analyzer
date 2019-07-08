package com.degree.eliif.hearinganalyzer

import android.graphics.*
import android.graphics.drawable.Drawable

class LineView : Drawable() {
  override fun draw(canvas: Canvas) {

    val paint = Paint()
    paint.color = Color.BLACK
    paint.strokeWidth = 4F
    paint.textSize = 30F

    val xVal = arrayListOf("125", "250", "500", "750", "1k", "1.5k", "2k", "3k", "4k", "8k", "9k", "10k", "11.2k", "14k", "16k")

    // Vertical
    for ((index, x) in (50..1020 step 60).withIndex()) {
      canvas.drawLine(x.toFloat(), 30F, x.toFloat(), 635F, paint)
      if (index != 0) {
        if (index - 1 < xVal.size) {
          canvas.drawText(xVal[index - 1], x.toFloat() - 20, 20F, paint)
        }
      }
    }

    val yVal = arrayListOf("-10", "0", "10", "20", "30", "40", "50", "60", "70", "80")

    // Horizontal
    for ((index, y) in (30..720 step 55).withIndex()) {
      canvas.drawLine(50F, y.toFloat(), 1010F, y.toFloat(), paint)

      // render y vals
      if (index != 0) {
        if (index - 1 < yVal.size) {
          canvas.drawText(yVal[index - 1], 0F, y.toFloat() + 10F, paint)
        }
      }
    }
    paint.color = Color.RED
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}