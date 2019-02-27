package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  lateinit var setupWave: PlayWave

  @TargetApi(Build.VERSION_CODES.O)
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setupWave = PlayWave()

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
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    if (parent?.id == R.id.frequencySpinner) {
      val frequency = parent.getItemAtPosition(pos)
      textView!!.text = frequency.toString()

      setupWave.setFrequency(frequency.toString().toInt())
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun onRadioButtonClicked(view: View) {
    if (view is RadioButton) {
      val checked = view.isChecked
      setupWave.stop()

      when (view.id) {
        R.id.left ->
          if (checked) {
            setupWave.setSide(true)
          }
        R.id.right -> {
          if (checked) {
            setupWave.setSide(false)
          }
        }
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun onClick(view: View) {
    setupWave.setWave()
    Thread.sleep(200)
    setupWave.play()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun stopClick(view: View) {
    setupWave.stop()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun less(view: View) {
    setupWave.less()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun more(view: View) {
    setupWave.more()
  }
}

