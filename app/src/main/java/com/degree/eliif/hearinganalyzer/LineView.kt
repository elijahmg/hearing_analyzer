package com.degree.eliif.hearinganalyzer

import android.graphics.*
import android.graphics.drawable.Drawable

class LineView : Drawable() {
  override fun draw(canvas: Canvas) {

    val paint = Paint()
    paint.color = Color.BLACK
    paint.strokeWidth = 5F

//    canvas.drawLine(0F, 0F, 1000F, 0F, paint)
//    canvas.drawLine(0F, 100F, 1000F, 100F, paint)
//    canvas.drawLine(0F, 200F, 1000F, 200F, paint)
//    canvas.drawLine(0F, 300F, 1000F, 300F, paint)
//    canvas.drawLine(0F, 400F, 1000F, 400F, paint)
//    canvas.drawLine(0F, bounds.top.toFloat(), bounds.left.toFloat(), bounds.bottom.toFloat(), paint)
//
//    canvas.drawLine(0F, 0F, 0F, 1000F, paint)
//    canvas.drawLine(100F, 0F, 100F, 1000F, paint)
//    canvas.drawLine(200F, 0F, 200F, 1000F, paint)
//    canvas.drawLine(300F, 0F, 300F, 1000F, paint)
//    canvas.drawLine(400F, 0F, 400F, 1000F, paint)
//    canvas.drawLine(500F, 0F, 500F, 1000F, paint)
//    canvas.drawLine(600F, 0F, 600F, 1000F, paint)
//    canvas.drawLine(700F, 0F, 700F, 1000F, paint)
//    canvas.drawLine(800F, 0F, 800F, 1000F, paint)
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}