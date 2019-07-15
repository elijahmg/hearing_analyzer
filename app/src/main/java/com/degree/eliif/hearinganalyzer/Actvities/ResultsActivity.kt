package com.degree.eliif.hearinganalyzer.Actvities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.degree.eliif.hearinganalyzer.LineView
import com.degree.eliif.hearinganalyzer.POJO.Coordination
import com.degree.eliif.hearinganalyzer.POJO.Result
import com.degree.eliif.hearinganalyzer.R
import com.google.gson.Gson
import java.io.*

class ResultsActivity : AppCompatActivity() {
  private lateinit var resultObj: Result
  private lateinit var resultCoordination: Coordination

  lateinit var GRAPH: ImageView
  private var RESULT_FILE = "result.json"

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_results)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val isLoadAllow = intent?.extras?.get("loadLast")

    GRAPH = findViewById(R.id.graphResult)

    if (isLoadAllow != null) {
      this.load()
    } else {
      resultObj = intent?.extras?.getSerializable("results") as Result
      resultCoordination = intent?.extras?.getSerializable("coordination") as Coordination
    }

    this.initializeGraph()
  }

  @RequiresApi(Build.VERSION_CODES.N)
  fun initializeGraph() {
    GRAPH.setImageDrawable(LineView(resultCoordination.leftCoordinatesXY, resultCoordination.rightCoordinatesXY))
  }


  /**
   * Save to external doc
   */
  fun saveToDoc(view: View) {
    val leftFile: FileOutputStream = openFileOutput(RESULT_FILE, Context.MODE_PRIVATE)

    try {
      val gson = Gson()
      val resultAsString = gson.toJson(resultCoordination)

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
  @RequiresApi(Build.VERSION_CODES.N)
  fun loadFromDoc(view: View) {
    this.load()

    this.initializeGraph()
  }

  private fun load() {
    val leftFile: FileInputStream = openFileInput(RESULT_FILE)

    val leftStream = InputStreamReader(leftFile)

    val resultsString = leftStream.buffered().use { it.readText() }

    val gson = Gson()

    resultCoordination = gson.fromJson(resultsString, Coordination::class.java)

    leftFile.close()
  }
}