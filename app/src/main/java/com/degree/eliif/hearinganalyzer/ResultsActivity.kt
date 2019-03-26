package com.degree.eliif.hearinganalyzer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_results.*
import java.io.*
import java.lang.StringBuilder

class ResultsActivity : AppCompatActivity() {

  private lateinit var resultObj: Result
  private var LEFT_FILE_NAME = "leftResult.txt"
  private var RIGHT_FILE_NAME = "rightResult.txt"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_results)

    val isLoadAllow = intent?.extras?.get("loadLast")

    if (isLoadAllow != null) {
      this.load()
    } else {
      resultObj = intent?.extras?.getSerializable("results") as Result
      var resultAsStringLeft = ""
      var resultAsStringRight = ""

      resultObj.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }
      resultObj.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }

      resultsTextViewRight?.text = resultAsStringRight
      resultsTextViewLeft?.text = resultAsStringLeft
    }
  }


  /**
   * Save to external doc
   */
  fun saveToDoc(view: View) {
    val leftFile: FileOutputStream = openFileOutput(LEFT_FILE_NAME, Context.MODE_PRIVATE)
    val rightFile: FileOutputStream = openFileOutput(RIGHT_FILE_NAME, Context.MODE_PRIVATE)

    try {
      var resultAsStringLeft = ""
      var resultAsStringRight = ""

      resultObj.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "," }
      resultObj.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "," }

      resultAsStringLeft += "\n"
      resultAsStringRight += "\n"

      leftFile.write(resultAsStringLeft.toByteArray())
      rightFile.write(resultAsStringRight.toByteArray())

      Toast.makeText(this, "File has been saved$filesDir", Toast.LENGTH_LONG).show()
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }

    leftFile.close()
    rightFile.close()
  }

  /**
   * Load from doc
   */
  fun loadFromDoc(view: View) {
    this.load()
  }

  private fun load () {
    val leftFile: FileInputStream = openFileInput(LEFT_FILE_NAME)
    val rightFile: FileInputStream = openFileInput(RIGHT_FILE_NAME)

    val leftStream = InputStreamReader(leftFile)
    val rightStream = InputStreamReader(rightFile)

    val resultsLeft = leftStream.buffered().use { it.readText() }
    val resultsRight = rightStream.buffered().use { it.readText() }
    Log.d("re", resultsLeft)

    resultsTextViewLeft?.text = resultsLeft.replace(",", "\n")
    resultsTextViewRight?.text = resultsRight.replace(",", "\n")

    leftFile.close()
    rightFile.close()
  }
}
