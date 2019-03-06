package com.degree.eliif.hearinganalyzer

import android.annotation.TargetApi
import android.media.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread
import kotlin.math.sign
import kotlin.text.Typography.amp

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlayWave {
  private val SAMPLE_RATE: Int = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC)
  private val mAudio: AudioTrack
  private val buffsize = AudioTrack.getMinBufferSize(
    SAMPLE_RATE,
    AudioFormat.CHANNEL_OUT_STEREO,
    AudioFormat.ENCODING_PCM_16BIT
  )

  private var sampleCount: Int = 32767

  private var FREQUENCY: Double = 440.0
  private var LEVEL: Double = 16384.0
  private var AMP: Float = 800F

  private var isPlaying = true

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
      .setTransferMode(AudioTrack.MODE_STREAM)
      .setBufferSizeInBytes(buffsize)
      .build()

    mAudio.setVolume(1F)
  }

  fun setSide(left: Boolean) {
    LEFT_CHANNEL = left
  }

  fun setFrequency(frequency: Double) {
    FREQUENCY = frequency
  }

  fun less() {
    LEVEL /= 2
  }

  fun more() {
    LEVEL *= 2
  }

  /**
   * Set up wave length
   */
  fun setWave() {
    val state = mAudio.state

    if (state != AudioTrack.STATE_INITIALIZED) {
      mAudio.release()
      return
    }

    isPlaying = true

    var sizes = arrayOf(1024, 2048, 4096, 8192, 16328, 32768)

    var size = 0

    for (s in sizes) {
      if (s > buffsize) {
        size = s
        break
      }
    }

    mAudio.play()

    val samples = ShortArray(size)

    var f = FREQUENCY
    var l = 0.0
    var q = 0.0

    val k = Math.PI / SAMPLE_RATE

    thread {
      while (isPlaying) {

        for (i in 0 until samples.size) {
          f += (FREQUENCY - f) / 4096.0
          l += (l * 16384 - l) / 4096.0
          q += if (q < Math.PI) f * k else (f * k) - (2.0 * Math.PI)

          when (LEFT_CHANNEL) {
            true -> {
              if (i % 2 == 0) {
                samples[i] = Math.round(Math.sin(q) * LEVEL).toShort()
              }
            }

            false -> {
              if (i % 2 != 0) {
                samples[i] = Math.round(Math.sin(q) * LEVEL).toShort()
              }
            }
          }
        }

        mAudio.write(samples, 0, samples.size)
      }
    }
  }

  fun stop() {
    isPlaying = false
    mAudio.stop()
    mAudio.flush()
  }
}