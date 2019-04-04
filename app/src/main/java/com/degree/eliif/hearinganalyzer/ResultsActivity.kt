package com.degree.eliif.hearinganalyzer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_results.*
import java.io.*

class ResultsActivity : AppCompatActivity() {

  private lateinit var resultObj: Result
  private var RESULT_FILE = "result.json"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_results)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

      var isBadResult = false
      resultObj.goodResults.forEach {
        if (resultObj.resultsLeft[it.key] != null) {
          isBadResult = resultObj.resultsLeft[it.key]!! < it.value
        }
      }

      testResult?.text = if (isBadResult) "You have to see specialist" else "Your hear is good"
    }
  }

  /**
   * Save to external doc
   */
  fun saveToDoc(view: View) {
    val leftFile: FileOutputStream = openFileOutput(RESULT_FILE, Context.MODE_PRIVATE)

    try {
      val gson = Gson()
      val resultAsString = gson.toJson(resultObj)

      leftFile.write(resultAsString.toByteArray())

      Toast.makeText(this, "File has been saved$filesDir", Toast.LENGTH_LONG).show()
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }

    leftFile.close()
  }

  /**
   * Load from doc
   */
  fun loadFromDoc(view: View) {
    this.load()
  }

  private fun load () {
    val leftFile: FileInputStream = openFileInput(RESULT_FILE)

    val leftStream = InputStreamReader(leftFile)

    val resultsString = leftStream.buffered().use { it.readText() }

    val gson = Gson()

    val result = gson.fromJson(resultsString, Result::class.java)


    var resultAsStringLeft = ""
    var resultAsStringRight = ""

    result.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }
    result.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }

    resultsTextViewRight?.text = resultAsStringRight
    resultsTextViewLeft?.text = resultAsStringLeft

    leftFile.close()
  }
}
