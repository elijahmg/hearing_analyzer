package com.degree.eliif.hearinganalyzer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_results.*

class ResultsActivity : AppCompatActivity() {

  private lateinit var resultObj: Result

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_results)

    resultObj = intent?.extras?.getSerializable("results") as Result
    var resultAsStringLeft = ""
    var resultAsStringRight = ""

    resultObj.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }
    resultObj.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }

    resultsTextViewRight?.text = resultAsStringRight
    resultsTextViewLeft?.text = resultAsStringLeft
  }
}
