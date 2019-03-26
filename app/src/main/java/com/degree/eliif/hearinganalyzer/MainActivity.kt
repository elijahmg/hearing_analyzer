package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  lateinit var setupWave: PlayWave
  lateinit var frequencySpinner: Spinner

  @TargetApi(Build.VERSION_CODES.O)
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setupWave = PlayWave()

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    this.initializeSpinner()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun initializeSpinner() {
    /** Create frequency spinner **/
    frequencySpinner = this.findViewById(R.id.frequencySpinner)
    ArrayAdapter.createFromResource(
      this,
      R.array.frequency_array,
      android.R.layout.simple_spinner_item
    ).also { adapter ->
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      frequencySpinner.adapter = adapter
    }

    frequencySpinner.onItemSelectedListener = this
    textView!!.text = setupWave.getLevelDb()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    if (parent?.id == R.id.frequencySpinner) {
      val frequency = parent.getItemAtPosition(pos)

      setupWave.currentIndex = pos
      setupWave.resetLevel()
      textView!!.text = setupWave.getLevelDb()
      setupWave.setFrequency(frequency.toString().toDouble())
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun onRadioButtonClicked(view: View) {
    if (view is RadioButton) {
      val checked = view.isChecked

      setupWave.resetLevel()
      frequencySpinner.setSelection(0)
      textView!!.text = setupWave.getLevelDb()

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
    textView!!.text = setupWave.getLevelDb()
    thread {
      setupWave.setWave()
      Thread.sleep(750)
      setupWave.mute()
      Thread.sleep(300)
      setupWave.stop()
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun less(view: View) {
    setupWave.less()
    textView!!.text = setupWave.getLevelDb()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun more(view: View) {
    setupWave.more()
    textView!!.text = setupWave.getLevelDb()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun precisionLess(view: View) {
    setupWave.precisionLess()
    textView!!.text = setupWave.getLevelDb()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun precisionMore(view: View) {
    setupWave.precisionMore()
    textView!!.text = setupWave.getLevelDb()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun save(view: View) {
    setupWave.saveResult()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun openResultActivity(view: View) {
    val resultIntent = Intent(this, ResultsActivity::class.java)
    resultIntent.putExtra("results", setupWave.getResult())
    startActivity(resultIntent)
  }
}

