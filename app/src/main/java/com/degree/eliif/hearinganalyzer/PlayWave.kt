package com.degree.eliif.hearinganalyzer

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.SoundPool
import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PlayWave {
  private val SAMPLE_RATE = 170000
  private val mAudio: AudioTrack
  private val buffsize = AudioTrack.getMinBufferSize(
    SAMPLE_RATE,
    AudioFormat.CHANNEL_OUT_MONO,
    AudioFormat.ENCODING_PCM_16BIT
  )

  private var LOOP_LENGTH = 1

  private var sampleCount: Int = 0

  private var FREQUENCY: Int = 1

  private var VOLUME: Float = 0F

  private var isPlaying = false

  init {
    mAudio = AudioTrack(
      AudioManager.STREAM_MUSIC,
      SAMPLE_RATE,
      AudioFormat.CHANNEL_OUT_STEREO,
      AudioFormat.ENCODING_PCM_16BIT,
      buffsize,
      AudioTrack.MODE_STATIC
    )

    mAudio.setVolume(0.0001F)
  }

  /**
   * Set up wave length
   */
  fun setWave(frequency: Int) {
    FREQUENCY = frequency
    sampleCount = SAMPLE_RATE / frequency
    val samples = ShortArray(sampleCount)
    print(samples)
    val amp = 32767
    val twopi = 8.0 * Math.atan(1.0)
    var phase = 0.0

    for (i in 0 until sampleCount) {
      samples[i] = (amp * Math.sin(phase)).toShort()
      phase += twopi * frequency / SAMPLE_RATE
    }

    mAudio.write(samples, 0, sampleCount)
  }

  /**
   * Set up volume
   */
  fun makeLouder(): Float {
    VOLUME += 0.001F
    if (VOLUME > 1 ) return 1.2F
    mAudio.setVolume(VOLUME)
    return VOLUME
  }

  fun makeQuiter(): Float {
    VOLUME -= 0.001F
    mAudio.setVolume(VOLUME)
    return VOLUME
  }

  fun resetVolume() {
    mAudio.setVolume(0F)
  }

  /** 1sec - 1 Hertz **/
  fun play() {
    mAudio.reloadStaticData()
    mAudio.setLoopPoints(0, sampleCount, -1)

    isPlaying = true

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
  }

}