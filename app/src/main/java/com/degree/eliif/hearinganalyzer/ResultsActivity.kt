package com.degree.eliif.hearinganalyzer

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_results.*
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import java.io.*

class ResultsActivity : AppCompatActivity() {

  private lateinit var resultObj: Result
  private var RESULT_FILE = "result.json"

  private lateinit var chart: LineChartView

  private var axisData: MutableList<Int> = mutableListOf(3, 6, 7, 9, 7)
  private var yAxisData: MutableList<Int> = mutableListOf(5, 6, 7, 9, 7)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_results)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    chart = findViewById(R.id.chart)

    val xAxisValues = mutableListOf<AxisValue>()
    val yAxisValues = mutableListOf<PointValue>()

    val line = Line(yAxisValues).setColor(Color.BLACK)

    for (i in 0 until axisData.size) {
      xAxisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i].toString()))
    }

    for (i in 0 until yAxisData.size) {
      yAxisValues.add(i, PointValue(i.toFloat(), yAxisData[i].toFloat()))
    }

    val lineChart = LineChartData()

    val lines: MutableList<Line> = mutableListOf()

    lines.add(line)

    lineChart.lines = lines


    val xAxis = Axis()
    xAxis.name = "Freq"
    lineChart.axisXTop = xAxis

    val xYxis = Axis()
    xYxis.name = "Db"
    lineChart.axisYLeft = xYxis


    chart.lineChartData = lineChart


    val isLoadAllow = intent?.extras?.get("loadLast")

    if (isLoadAllow != null) {
      this.load()
    } else {
      resultObj = intent?.extras?.getSerializable("results") as Result
      var resultAsStringLeft = ""
      var resultAsStringRight = ""

      resultObj.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }
      resultObj.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }

//       resultsTextViewRight?.text = resultAsStringRight
      //   resultsTextViewLeft?.text = resultAsStringLeft

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

  private fun load() {
    val leftFile: FileInputStream = openFileInput(RESULT_FILE)

    val leftStream = InputStreamReader(leftFile)

    val resultsString = leftStream.buffered().use { it.readText() }

    val gson = Gson()

    val result = gson.fromJson(resultsString, Result::class.java)


    var resultAsStringLeft = ""
    var resultAsStringRight = ""

    result.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }
    result.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }

    // resultsTextViewRight?.text = resultAsStringRight
    // resultsTextViewLeft?.text = resultAsStringLeft

    leftFile.close()
  }
}
