package com.degree.eliif.hearinganalyzer

import android.graphics.*
import android.graphics.drawable.Drawable

class LineView : Drawable() {
  override fun draw(canvas: Canvas) {

    val paint = Paint()
    paint.color = Color.BLACK
    paint.strokeWidth = 2F

    for (x in 0..1000 step 70) {
      canvas.drawLine(0F, x.toFloat(), 1000F, x.toFloat(), paint)
      canvas.drawLine(x.toFloat(), 0F, x.toFloat(), 1000F, paint)
    }
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}