package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.degree.eliif.hearinganalyzer.R.id.textView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  lateinit var setupWave: PlayWave
  lateinit var seekBar: SeekBar

  @TargetApi(Build.VERSION_CODES.O)
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setupWave = PlayWave()

    this.initializeSeekBar()
    this.initializeSpinner()
  }

  private fun initializeSpinner() {
    /** Create frequency spinner **/
    val frequencySpinner: Spinner = this.findViewById(R.id.frequencySpinner)
    ArrayAdapter.createFromResource(
      this,
      R.array.frequency_array,
      android.R.layout.simple_spinner_item
    ).also { adapter ->
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      frequencySpinner.adapter = adapter
    }

    frequencySpinner.onItemSelectedListener = this
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun initializeSeekBar() {
    /** Initialize seek bar **/
    seekBar = findViewById(R.id.seekBar)

    seekBar.max = 100
    seekBar.progress = 100

    seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
        textView!!.text = (progress * -1).toString()

        setupWave.setVolume((progress * -1).toString().toInt())
      }

      override fun onStartTrackingTouch(p0: SeekBar?) {}

      override fun onStopTrackingTouch(p0: SeekBar?) {}
    })
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    if (parent?.id == R.id.frequencySpinner) {
      val frequency = parent.getItemAtPosition(pos)
      textView!!.text = frequency.toString()

      setupWave.setWave(frequency.toString().toInt())
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun onClick(view: View) {
     setupWave.stop()
     setupWave.play()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun stopClick(view: View) {
    setupWave.stop()
  }
}

