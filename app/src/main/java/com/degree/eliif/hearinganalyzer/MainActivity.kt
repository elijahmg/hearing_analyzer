package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
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
    Log.d("create", savedInstanceState.toString())


    setContentView(R.layout.activity_main)

    setupWave = PlayWave()

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    this.initializeSpinner()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    outState?.putInt("pos", 3)
    Log.d("saving", outState.toString())
    super.onSaveInstanceState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    super.onRestoreInstanceState(savedInstanceState)
    Log.d("restoring", savedInstanceState.toString())
    Toast.makeText(this, "Resotring", Toast.LENGTH_LONG).show()
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
  fun setNextFrequency(view: View) {
    val spinnerLength = frequencySpinner.adapter.count

    if (setupWave.currentIndex + 1 > spinnerLength - 1) {
      Toast.makeText(this, "This is the last frequency", Toast.LENGTH_LONG).show()
    } else {
      setupWave.currentIndex += 1

      frequencySpinner.setSelection(setupWave.currentIndex)
      val frequency = frequencySpinner.getItemAtPosition(setupWave.currentIndex)

      setupWave.resetLevel()
      setupWave.setFrequency(frequency.toString().toDouble())
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun setPreviousFrequency(view: View) {
    if (setupWave.currentIndex - 1 < 0) {
      Toast.makeText(this, "This is the first frequency", Toast.LENGTH_LONG).show()
    } else {
      setupWave.currentIndex -= 1

      frequencySpinner.setSelection(setupWave.currentIndex)
      val frequency = frequencySpinner.getItemAtPosition(setupWave.currentIndex)

      setupWave.resetLevel()
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

    Toast.makeText(this, "Results have been saved", Toast.LENGTH_SHORT).show()

    this.setNextFrequency(view)
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun openResultActivity(view: View) {
    val resultIntent = Intent(this, ResultsActivity::class.java)
    resultIntent.putExtra("results", setupWave.getResult())
    startActivity(resultIntent)
  }
}