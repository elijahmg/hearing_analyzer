package com.degree.eliif.hearinganalyzer

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.degree.eliif.hearinganalyzer.R.id.textView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  lateinit var setupWave: PlayWave
  lateinit var sp: SoundPool

  var isPlaying = true

  var sound: Int = 0

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setupWave = PlayWave()

    sp = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
    // sp.setOnLoadCompleteListener(this)

    sound = sp.load(this, R.raw.shoot, 0)


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

      setupWave.setWave(frequency.toString().toInt())
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun onClick(view: View) {
    // setupWave.stop()
    // setupWave.play()
    sp.play(sound, 0F, 1F, 0, -1, 1F)
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun stopClick(view: View) {
    // setupWave.stop()
    sp.stop(sound)
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun louderOnClick(view: View) {
    textView!!.text = setupWave.makeLouder().toString()
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun quieterOnClick(view: View) {
    textView!!.text = setupWave.makeQuiter().toString()
  }
}

private fun SoundPool.setOnLoadCompleteListener(mainActivity: MainActivity) {

}
