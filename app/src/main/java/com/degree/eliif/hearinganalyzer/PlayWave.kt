package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.media.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread
import kotlin.text.Typography.amp

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlayWave {
  private val SAMPLE_RATE: Int = 170000
  private val mAudio: AudioTrack
  private val buffsize = AudioTrack.getMinBufferSize(
    SAMPLE_RATE,
    AudioFormat.CHANNEL_OUT_STEREO,
    AudioFormat.ENCODING_PCM_16BIT
  )

  private var sampleCount: Int = 0

  private var FREQUENCY: Int = 0
  private var AMP: Float = 800F

  private var isPlaying = false

  private var LEFT_CHANNEL = true


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
        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
        .build())
      .setTransferMode(AudioTrack.MODE_STATIC)
      .setBufferSizeInBytes(buffsize)
      .build()

    mAudio.setVolume(1F)
  }

  fun setSide(left: Boolean) {
    LEFT_CHANNEL = left
  }

  fun setFrequency(frequency: Int) {
    FREQUENCY = frequency
  }

  fun less() {
    AMP /= 2
  }

  fun more() {
    AMP *= 2
  }

  /**
   * Set up wave length
   */
  fun setWave() {
    sampleCount = SAMPLE_RATE / FREQUENCY
    val samples = ShortArray(sampleCount)
    val pi = 2 * Math.PI
    var phase = 0.0

    when (LEFT_CHANNEL) {
      true -> {
        for (i in 0 until sampleCount) {
          if (i % 2 == 0) {
            samples[i] = (AMP * Math.sin(phase)).toShort()
            phase += pi * FREQUENCY / SAMPLE_RATE
          }
        }
      }

      false -> {
        for (i in 0 until sampleCount) {
          if (i % 2 != 0) {
            samples[i] = (AMP * Math.sin(phase)).toShort()
            phase += pi * FREQUENCY / SAMPLE_RATE
          }
        }
      }
    }

    mAudio.write(samples, 0, sampleCount)
  }

  /** 1sec - 1 Hertz **/
  fun play() {
    mAudio.reloadStaticData()
    mAudio.setLoopPoints(0, sampleCount, -1)

    thread {
      mAudio.play()
      Thread.sleep(750)
      mAudio.stop()
      mAudio.flush()
    }
  }

  fun stop() {
    isPlaying = false
  }
}