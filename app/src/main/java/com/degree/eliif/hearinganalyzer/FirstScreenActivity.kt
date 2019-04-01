package com.degree.eliif.hearinganalyzer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class FirstScreenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.first_screen)
  }

  fun startAnalyzerActivity(view: View) {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
  }

  fun startResultActivity(view: View) {
    val intent = Intent(this, ResultsActivity::class.java)
    intent.putExtra("loadLast", true)
    startActivity(intent)
  }
}
