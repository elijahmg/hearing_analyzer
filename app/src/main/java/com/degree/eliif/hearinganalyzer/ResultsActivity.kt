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
    var resultAsString = ""

    for (i in 0 until resultObj.results.size) {
      resultAsString += resultObj.results[i]
    }

    resultsTextView?.text = resultAsString
  }
}
