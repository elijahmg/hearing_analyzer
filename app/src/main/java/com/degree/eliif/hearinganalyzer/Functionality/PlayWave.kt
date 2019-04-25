package com.degree.eliif.hearinganalyzer.Functionality

import android.annotation.TargetApi
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import androidx.annotation.RequiresApi
import com.degree.eliif.hearinganalyzer.POJO.Result
import kotlin.concurrent.thread

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

  var FREQUENCY: Double = 400.0

  var LEVEL: Double = 327.67

  var NULL_LEVEL: Double = 0.0

  private var MUTE: Boolean = false

  private var isPlaying = true

  var LEFT_CHANNEL = true

  private var koef = (Math.pow(10.0, 0.5)).toFloat() // 10 db HL

  var currentIndex: Int = 0

  private var result = Result()

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

  fun setResult(result: Result) {
    this.result = result
  }

  /**
   * Reduce by 5db
   */
  fun less() {
    LEVEL /= koef
  }

  /**
   * Increase by 5db
   */
  fun more() {
    LEVEL *= koef
  }

  /**
   * Return result object
   */
  fun getResult(): Result {
    return result
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
    MUTE = false

    val sizes = arrayOf(1024, 2048, 4096, 8192, 16328, 32768)

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

        /** Create sin wave **/
        for (i in 0 until samples.size) {
          f += (FREQUENCY - f) / 4096
          l += ((if (MUTE) 0.0 else LEVEL) - l) / 4096
          q += if (q < Math.PI) f * k else (f * k) - (2.0 * Math.PI)

          when (LEFT_CHANNEL) {
            true -> {
              if (i % 2 == 0) {
                samples[i] = Math.round(Math.sin(q) * l).toShort()
              }
            }

            false -> {
              if (i % 2 != 0) {
                samples[i] = Math.round(Math.sin(q) * l).toShort()
              }
            }
          }
        }

        mAudio.write(samples, 0, samples.size)
      }
    }
  }

  /**
   * Return level in dB
   */
  fun getLevelDb(): String {
    return Math.round(20 * Math.log10(LEVEL / Short.MAX_VALUE)).toString() + "dB" + " / " + Math.round(LEVEL).toString()
  }

  /**
   * Set level back to -40 dB
   */
  fun resetLevel() {
    LEVEL = 327.67
  }

  /**
   * Mute signal
   */
  fun mute() {
    MUTE = true
  }

  /**
   * Stop playing signal
   */
  fun stop() {
    isPlaying = false
    mAudio.stop()
    mAudio.flush()
  }

  /**
   * Save result to result object
   */
  fun saveResult() {
    val level = Math.round(20 * Math.log10(LEVEL / NULL_LEVEL))
    if (LEFT_CHANNEL) {
      result.resultsLeft[FREQUENCY] = level
    } else {
      result.resultsRight[FREQUENCY] = level
    }
  }

  fun getDbHl(): String {
    return Math.round(20 * Math.log10(LEVEL / NULL_LEVEL)).toString() + "dB HL"
  }
}