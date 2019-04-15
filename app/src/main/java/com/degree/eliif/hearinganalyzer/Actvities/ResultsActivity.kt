package com.degree.eliif.hearinganalyzer.Actvities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.degree.eliif.hearinganalyzer.POJO.Result
import com.degree.eliif.hearinganalyzer.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import java.io.*

class ResultsActivity : AppCompatActivity() {

  private lateinit var resultObj: Result
  private var RESULT_FILE = "result.json"

  private lateinit var lineChart: LineChart

  private var axisData: MutableList<Int> = mutableListOf(3, 6, 7, 9, 7)
  private var yAxisData: MutableList<Int> = mutableListOf(5, 6, 7, 9, 7)

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

      resultObj.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB HL" + "\n" }
      resultObj.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB HL" + "\n" }
    }
    this.initializeChart()
  }

  private fun initializeChart() {
    lineChart = findViewById(R.id.lineChart)

    val rightResultsChart = mutableListOf<Entry>()
    val leftResultsChart = mutableListOf<Entry>()

    resultObj.resultsLeft.map { (k, v) -> leftResultsChart.add(Entry(k.toFloat(), v.toFloat())) }
    resultObj.resultsRight.map { (k, v) -> rightResultsChart.add(Entry(k.toFloat(), v.toFloat())) }

    val leftSet = LineDataSet(leftResultsChart, "Left ear")
    leftSet.color = Color.BLUE
    leftSet.fillAlpha = 110

    val rightSet = LineDataSet(rightResultsChart, "Right ear")
    rightSet.color = Color.RED
    rightSet.fillAlpha = 110


    val dataSets = mutableListOf<ILineDataSet>()
    dataSets.add(leftSet)
    dataSets.add(rightSet)

    val lineData = LineData(dataSets)

    val yAxis = lineChart.axisLeft

    lineChart.axisLeft.isInverted = true
    lineChart.axisRight.isEnabled = false

    lineChart.xAxis.axisMinimum = 20F
    lineChart.xAxis.axisMaximum = 18000F

    lineChart.axisLeft.axisMinimum = -5F
    lineChart.axisLeft.axisMaximum = 100F

    lineChart.description = null

    lineChart.data = lineData
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

    resultObj = gson.fromJson(resultsString, Result::class.java)

    var resultAsStringLeft = ""
    var resultAsStringRight = ""

    resultObj.resultsLeft.map { (k, v) -> resultAsStringLeft += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }
    resultObj.resultsRight.map { (k, v) -> resultAsStringRight += k.toString() + "Hz: " + v.toString() + "dB" + "\n" }


    leftFile.close()
  }
}

//
//chart = findViewById(R.id.chart)
//
//val xAxisValues = mutableListOf<AxisValue>()
//val yAxisValues = mutableListOf<PointValue>()
//
//val line = Line(yAxisValues).setColor(Color.BLACK)
//
//for (i in 0 until axisData.size) {
//  xAxisValues.add(i, AxisValue(i.toFloat()).setLabel(axisData[i].toString()))
//}
//
//for (i in 0 until yAxisData.size) {
//  yAxisValues.add(i, PointValue(i.toFloat(), yAxisData[i].toFloat()))
//}
//
//val lineChart = LineChartData()
//
//val lines: MutableList<Line> = mutableListOf()
//
//lines.add(line)
//
//lineChart.lines = lines
//
//
//val xAxis = Axis()
//xAxis.name = "Freq"
//lineChart.axisXTop = xAxis
//
//val xYxis = Axis()
//xYxis.name = "Db"
//lineChart.axisYLeft = xYxis
//
//
//chart.lineChartData = lineChart
//
