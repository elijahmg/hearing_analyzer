package com.degree.eliif.hearinganalyzer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_results.*
import java.io.*
import java.lang.StringBuilder

class ResultsActivity : AppCompatActivity() {

  private lateinit var resultObj: Result
  private var FILE_NAME = "result.txt"

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


  /**
   * Save to external doc
   */
  fun saveToDoc(view: View) {
    val fos: FileOutputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)

    try {
      fos.write(resultObj.resultsLeft.toString().toByteArray())

      Toast.makeText(this, "File has been saved", Toast.LENGTH_LONG).show()
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }

    fos.close()
  }

  /**
   * Load from doc
   */
  fun loadFromDoc(view: View) {
    val fis: FileInputStream = openFileInput(FILE_NAME)

    val ipr = InputStreamReader(fis)
    val br = BufferedReader(ipr)

    val strB = StringBuilder()
    var string: String

    while (br.readLine() != null) {
      string = br.readLine()
      strB.append(string).append("\n")
    }

    resultsTextViewRight?.text = strB.toString()
  }
}
