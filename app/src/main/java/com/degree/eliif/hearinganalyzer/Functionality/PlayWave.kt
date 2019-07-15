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
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin

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

  var LEVEL: Float = 0.05F

  var NULL_LEVEL: Float = 0.0F

  private var MUTE: Boolean = false

  private var isPlaying = true

  var LEFT_CHANNEL = true

  private var koef = (10.0.pow(0.25)).toFloat() // 5 db HL

  var currentIndex: Int = 4 //start from 1 kHz

  var fromStart = true

  private var result = Result()

  /** Builder **/
  init {
    mAudio = AudioTrack.Builder()
      .setAudioAttributes(AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build())
      .setAudioFormat(AudioFormat.Builder()
        .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
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
  fun less(isCalibration: Boolean?) {

    if (!isCalibration!!) {
      val tempLevel = LEVEL / koef
      val tempdbHLLevel = (20 * log10(((tempLevel / NULL_LEVEL).toDouble()))).roundToInt()

      if (tempdbHLLevel < -10) return
    }

    LEVEL /= koef
  }

  /**
   * Increase by 5db
   */
  fun more(isCalibration: Boolean?) {

    if (!isCalibration!!) {
      val tempLevel = LEVEL * koef
      val tempdbHLLevel = (20 * log10(((tempLevel / NULL_LEVEL).toDouble()))).roundToInt()

      if (tempdbHLLevel > 95) return
    }

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

    val samples = FloatArray(size)

    var f = FREQUENCY
    var l = 0.0F
    var q = 0.0

    val k = (Math.PI / SAMPLE_RATE).toFloat()

    thread {
      while (isPlaying) {

        /** Create sin wave **/
        for (i in 0 until samples.size) {
          f += (FREQUENCY - f) / 4096
          l += ((if (MUTE) 0.0F else LEVEL) - l) / 4096
          q += if (q < Math.PI) f * k else (f * k) - (2.0 * Math.PI)

          when (LEFT_CHANNEL) {
            true -> {
              if (i % 2 == 0) {
                samples[i] = (sin(q) * l).toFloat()
              }
            }

            false -> {
              if (i % 2 != 0) {
                samples[i] = (sin(q) * l).toFloat()
              }
            }
          }
        }

        mAudio.write(samples, 0, samples.size, AudioTrack.WRITE_BLOCKING)
      }
    }
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
    val level = Math.round(20 * log10(((LEVEL / NULL_LEVEL).toDouble())))
    if (LEFT_CHANNEL) {
      result.resultsLeft[FREQUENCY] = level
    } else {
      result.resultsRight[FREQUENCY] = level
    }
  }

  fun getRawDbHl(): Int {
    return (20 * log10(((LEVEL / NULL_LEVEL).toDouble()))).roundToInt()
  }

  fun getDbHl(): String {
    return (20 * log10(((LEVEL / NULL_LEVEL).toDouble()))).roundToInt().toString() + "dB HL"
  }
}