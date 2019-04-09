package com.degree.eliif.hearinganalyzer.Actvities

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.degree.eliif.hearinganalyzer.R

class FirstScreenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.first_screen)

    val manager =  getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    manager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)

    Toast.makeText(this, "Volume of your device has been set to max", Toast.LENGTH_LONG).show()
  }

  fun startAnalyzerActivity(view: View) {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)

    val sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
  }

  fun startCalibration(view: View) {
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra("calibration", true)
    startActivity(intent)

    val sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()
  }


  fun startResultActivity(view: View) {
    val intent = Intent(this, ResultsActivity::class.java)
    intent.putExtra("loadLast", true)
    startActivity(intent)
  }
}
