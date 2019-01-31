package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.content.Context
import android.media.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlayWave {
  private val SAMPLE_RATE = 170000
  private val mAudio: AudioTrack
  private val buffsize = AudioTrack.getMinBufferSize(
    SAMPLE_RATE,
    AudioFormat.CHANNEL_OUT_MONO,
    AudioFormat.ENCODING_PCM_16BIT
  )

  private var sampleCount: Int = 0

  private var FREQUENCY: Int = 1

  private var isPlaying = false

  /** Builder **/
  init {
    mAudio = AudioTrack.Builder()
      .setAudioAttributes(AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build())
      .setAudioFormat(AudioFormat.Builder()
        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
        .setSampleRate(SAMPLE_RATE)
        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
        .build())
      .setTransferMode(AudioTrack.MODE_STATIC)
      .setBufferSizeInBytes(buffsize)
      .build()

    mAudio.setVolume(0F)
  }

  /**
   * Set up wave length
   */
  fun setWave(frequency: Int) {
    FREQUENCY = frequency
    sampleCount = ((SAMPLE_RATE / frequency).toFloat()).toInt()
    val samples = ShortArray(sampleCount)

    val amp = 32767
    val pi = 8.0 * Math.atan(1.0)
    var phase = 0.0

    for (i in 0 until sampleCount) {
      samples[i] = (amp * Math.sin(phase)).toShort()
      phase += pi * frequency / SAMPLE_RATE
    }

    mAudio.write(samples, 0, sampleCount)
  }

  fun resetVolume() {
    mAudio.setVolume(0F)
  }

  /** 1sec - 1 Hertz **/
  fun play() {
    mAudio.reloadStaticData()
    mAudio.setLoopPoints(0, sampleCount, -1)

    isPlaying = true
     // mAudio.play()

    thread {
      while (isPlaying) {
        mAudio.play()
        Thread.sleep(250)
        mAudio.pause()
        // mAudio.flush()
        Thread.sleep(250)
      }
    }
  }

  fun stop() {
    isPlaying = false
//     mAudio.stop()
//     mAudio.flush()
  }

  fun setVolume(inputDb: Int) {
    Log.d("Input db", inputDb.toString())
    Log.d("db", ((inputDb.toFloat()) / 20).toDouble().toString())
    Log.d("db withour double", (inputDb / 20).toString())

    val gain = Math.pow(10.toDouble(), ((inputDb.toFloat())/20).toFloat().toDouble())
    Log.d("gain", gain.toString())
    mAudio.setVolume(gain.toFloat())
  }

}