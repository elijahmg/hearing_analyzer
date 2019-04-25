package com.degree.eliif.hearinganalyzer.Actvities

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.degree.eliif.hearinganalyzer.Dialogs.CalibrationDialog
import com.degree.eliif.hearinganalyzer.POJO.Calibration
import com.degree.eliif.hearinganalyzer.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.first_screen.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader

class FirstScreenActivity : AppCompatActivity(), CalibrationDialog.CalibrationDialogListener {

  val CALIBRATION_FILE = "customCalibration.json"

  var calibrationPOJO = Calibration()

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.first_screen)

    val manager =  getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    manager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)

    Toast.makeText(this, "Volume of your device has been set to max", Toast.LENGTH_LONG).show()

    this.initializeListeners()
  }

  /**
   * Initialize listeners for buttons
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun initializeListeners() {
    val listener = View.OnClickListener { v: View? ->
      when (v?.id) {
        R.id.startTest -> startTestListener()
      }
    }

    startTest?.setOnClickListener(listener)
  }

  fun startTestActivity(defaultCalibration: Boolean) {
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra("defaultCalibration", defaultCalibration)
    startActivity(intent)

    val sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
  }

  fun startCalibration(view: View) {
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra("calibration", true)
    intent.putExtra("defaultCalibration", false)
    startActivity(intent)

    val sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
  }


  fun startResultActivity(view: View) {
    val intent = Intent(this, ResultsActivity::class.java)
    intent.putExtra("loadLast", true)
    startActivity(intent)
  }

  private fun startTestListener() {
    val calibrationDialog = CalibrationDialog()

    calibrationDialog.show(supportFragmentManager, "calibrationDialog")
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun setChoice(choice: Boolean) {

    if (choice) {
      this.startTestActivity(true)
    } else {
      if (this.ifCustomExists()) {
        this.startTestActivity(false)
      }
    }
  }

  /**
   * Check if file exists
   */
  @TargetApi(Build.VERSION_CODES.N)
  private fun ifCustomExists(): Boolean {
    try {
      val file: FileInputStream = openFileInput(CALIBRATION_FILE)

      val leftStream = InputStreamReader(file)

      val resultsString = leftStream.buffered().use { it.readText() }

      val gson = Gson()

      calibrationPOJO = gson.fromJson(resultsString, Calibration::class.java)

      if (calibrationPOJO.calibrationLeft.size < 16) {
        Toast.makeText(this,"Custom calibration is not containing all values", Toast.LENGTH_SHORT).show()
        return false
      }

      if (calibrationPOJO.calibrationRight.size < 16) {
        Toast.makeText(this,"Custom calibration is not containing all values", Toast.LENGTH_SHORT).show()
        return false
      }

    } catch (e: FileNotFoundException) {
      Toast.makeText(this,"Custom file wasn't found", Toast.LENGTH_SHORT).show()
      return false
    }
    return true
  }
}
