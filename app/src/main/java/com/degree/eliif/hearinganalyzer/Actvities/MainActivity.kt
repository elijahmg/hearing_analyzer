package com.degree.eliif.hearinganalyzer.Actvities

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.degree.eliif.hearinganalyzer.Dialogs.VolumeDialog
import com.degree.eliif.hearinganalyzer.Functionality.Computate
import com.degree.eliif.hearinganalyzer.Functionality.PlayWave
import com.degree.eliif.hearinganalyzer.POJO.Calibration
import com.degree.eliif.hearinganalyzer.POJO.Result
import com.degree.eliif.hearinganalyzer.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

  lateinit var setupWave: PlayWave
  var calibrationPOJO = Calibration()

  val CALIBRATION_FILE = "customCalibration.json"

  lateinit var frequencySpinner: Spinner

  lateinit var leftProgress: ProgressBar
  lateinit var rightProgress: ProgressBar

  val calibSpl: EditText by lazy { findViewById<EditText>(R.id.calibSpl) }
  val calibHl: EditText by lazy { findViewById<EditText>(R.id.calibHl) }

  var isCalibration = false as Boolean?
  var isDefaultCalibration = true as Boolean?

  @TargetApi(Build.VERSION_CODES.O)
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    setupWave = PlayWave()

    leftProgress = findViewById(R.id.leftProgress)
    rightProgress = findViewById(R.id.rightProgress)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    isCalibration = intent?.extras?.getBoolean("calibration")

    isDefaultCalibration = intent?.extras?.getBoolean("defaultCalibration")

    if (isDefaultCalibration == true) {
      this.loadDefaultCalibration()
    }

    if (isCalibration !== null && isCalibration == true) {
      this.setVisibility()
    }

    this.initializeSpinner()
    this.initializeListeners()
  }

  /**
   * Initialize listeners for buttons
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun initializeListeners() {
    val listener = View.OnClickListener { v: View? ->
      when (v?.id) {
        R.id.nextFq -> setNextFrequency()
        R.id.saveCalibrations -> saveCalibration()
        R.id.finishCalib -> finishCalibration()
        R.id.finishTest -> openResultActivity()
        R.id.play -> playSignal()
      }
    }

    nextFq?.setOnClickListener(listener)
    saveCalibrations?.setOnClickListener(listener)
    finishCalib?.setOnClickListener(listener)
    finishTest?.setOnClickListener(listener)
    play?.setOnClickListener(listener)
  }

  /**
   * In case of calibration set visibility of layout
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private fun setVisibility() {
    val calibrationLayout = findViewById<ConstraintLayout>(R.id.calibrationLayout)

    val saveResultButton = findViewById<Button>(R.id.saveResult)

    val finishTestButton = findViewById<Button>(R.id.finishTest)

    calibrationLayout.visibility = View.VISIBLE

    // textView.visibility = View.INVISIBLE

    // lessVolume.isEnabled = false
    // moreVolume.isEnabled = false

    saveResultButton.isEnabled = false

    finishTestButton.isEnabled = false


    /** Set listeners for keyboard buttons **/
    calibSpl.setOnEditorActionListener { v, actionId, event ->
      return@setOnEditorActionListener when (actionId) {
        EditorInfo.IME_ACTION_NEXT -> {
          calibHl.requestFocus()
        }
        else -> false
      }
    }

    /** Set listeners for keyboard buttons **/
    calibHl.setOnEditorActionListener { v, actionId, event ->
      return@setOnEditorActionListener when (actionId) {
        EditorInfo.IME_ACTION_DONE -> {
          this.saveCalibration()
          this.setNextFrequency()

          calibHl.text = null
          calibSpl.text = null

          calibSpl.requestFocus()
          Toast.makeText(this, "Keep calibrating", Toast.LENGTH_SHORT).show()
          true
        }
        else -> false
      }
    }
  }

  /**
   * Initialize spinner
   */
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

    this.resetLevel()

    /** Restore activity state **/
    this.setSharedValues()
  }

  /**
   * Saving shared values in case of pressing home button
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun setSharedValues() {
    val sharedPref = getSharedPreferences("share", Context.MODE_PRIVATE) ?: return
    val wasSet = sharedPref.getBoolean("wasSet", false)

    if (!wasSet) {
      return
    }

    val position = sharedPref.getInt("pos", 0)
    val resultAsString = sharedPref.getString("result", "")
    val side = sharedPref.getBoolean("side", true)

    isDefaultCalibration = sharedPref.getBoolean("isDefaultCalibration", true)
    isCalibration = sharedPref.getBoolean("isCalibration", false)

    if (isDefaultCalibration == true) {
      this.loadDefaultCalibration()
    }

    /** Setting frequency **/
    if (position != -1) {
      frequencySpinner.setSelection(position)
      val frequency = frequencySpinner.getItemAtPosition(position)

      this.resetLevel()
      setupWave.FREQUENCY = frequency.toString().toDouble()
    }

    /** Setting object **/
    if (resultAsString != "") {
      val gson = Gson()
      val resultAsObject = gson.fromJson(resultAsString, Result::class.java)

      setupWave.setResult(resultAsObject)
    }

    leftProgress.progress = sharedPref.getInt("leftProgress", 0)
    rightProgress.progress = sharedPref.getInt("rightProgress", 0)

    val left = findViewById<RadioButton>(R.id.left)
    val right = findViewById<RadioButton>(R.id.right)

    left.isChecked = side
    right.isChecked = !side
  }

  /**
   * Spinner listener
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    if (parent?.id == R.id.frequencySpinner) {
      setupWave.currentIndex = pos
      setupWave.FREQUENCY = frequencySpinner.getItemAtPosition(pos).toString().toDouble()

      if (isCalibration !== null && isCalibration == false) {
        this.resetLevel()
        textView!!.text = setupWave.getDbHl()
      } else {
        textView!!.text = setupWave.LEVEL.toString()
      }
    }
  }

  fun resetLevel() {
    if (isCalibration !== null && isCalibration == true) {
      setupWave.LEVEL = 0.05F
    } else {
      val frequency = frequencySpinner.getItemAtPosition(setupWave.currentIndex)
      val frequencyHz = frequency.toString().toDouble()
      val levelInFloat = Computate(calibrationPOJO).getFloatLevelForNullSpl(frequencyHz, setupWave.LEFT_CHANNEL)

      setupWave.NULL_LEVEL = levelInFloat!!
      setupWave.LEVEL = levelInFloat * 100 // set at 40 dB HL
    }
  }

  /**
   * Set next frequency
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun setNextFrequency() {
    val spinnerLength = frequencySpinner.adapter.count

    if (setupWave.currentIndex + 1 > spinnerLength - 1) {
      Toast.makeText(this, "This is the last frequency", Toast.LENGTH_LONG).show()
    } else {
      setupWave.currentIndex += 1

      frequencySpinner.setSelection(setupWave.currentIndex)
      val frequency = frequencySpinner.getItemAtPosition(setupWave.currentIndex)

      setupWave.FREQUENCY = frequency.toString().toDouble()
    }
  }

  /**
   * Set previous frequency
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun setPreviousFrequency(view: View) {
    if (setupWave.currentIndex - 1 < 0) {
      Toast.makeText(this, "This is the first frequency", Toast.LENGTH_LONG).show()
    } else {
      setupWave.currentIndex -= 1

      frequencySpinner.setSelection(setupWave.currentIndex)
      val frequency = frequencySpinner.getItemAtPosition(setupWave.currentIndex)

      setupWave.FREQUENCY = frequency.toString().toDouble()
    }
  }

  /**
   * On radio button listener
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun onRadioButtonClicked(view: View) {
    if (view is RadioButton) {
      val checked = view.isChecked

      frequencySpinner.setSelection(0)
      textView!!.text = setupWave.getDbHl()

      when (view.id) {
        R.id.left ->
          if (checked) {
            setupWave.LEFT_CHANNEL = true
          }
        R.id.right -> {
          if (checked) {
            setupWave.LEFT_CHANNEL = false
          }
        }
      }
      this.resetLevel()
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  /**
   * On play listener
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun playSignal() {
    if (isCalibration !== null && isCalibration == false) {
      textView!!.text = setupWave.getDbHl()
    }

    thread {
      setupWave.setWave()
      Thread.sleep(750)
      setupWave.mute()
      Thread.sleep(300)
      setupWave.stop()
    }
  }

  /**
   * Set reduce volume
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun less(view: View) {
    setupWave.less()

    if (isCalibration !== null && isCalibration == true) {
      textView!!.text = setupWave.LEVEL.toString()
    } else {
      textView!!.text = setupWave.getDbHl()
    }
  }

  /**
   * Set increase volume
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun more(view: View) {
    setupWave.more()
    if (isCalibration !== null && isCalibration == true) {
      textView!!.text = setupWave.LEVEL.toString()
    } else {
      textView!!.text = setupWave.getDbHl()
    }
  }

  /**
   * Save measurement result
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun save(view: View) {
    setupWave.saveResult()

    when (setupWave.LEFT_CHANNEL) {
      true -> {
        leftProgress.progress = setupWave.getResult().resultsLeft.size
      }

      false -> {
        rightProgress.progress = setupWave.getResult().resultsRight.size
      }
    }

    Toast.makeText(this, "Results have been saved", Toast.LENGTH_SHORT).show()

    this.setNextFrequency()
  }

  /**
   * Volume key listener
   */
  override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
    if (event?.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
      || event?.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
      val dialog = VolumeDialog()
      dialog.show(supportFragmentManager, "volume")

      val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
      val maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
      manager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }

    return false
  }

  /**
   * Save calibration listener
   */
  private fun saveCalibration() {

    val left = setupWave.LEFT_CHANNEL
    val frequency = setupWave.FREQUENCY

    val dbSPL = calibSpl.text
    val dbHl = calibHl.text

    val levelInFloat = setupWave.LEVEL

    when (left) {
      true -> {
        calibrationPOJO.calibrationLeft[frequency] = dbSPL.toString().toFloat()
        leftProgress.progress = calibrationPOJO.calibrationLeft.size
      }

      false -> {
        calibrationPOJO.calibrationRight[frequency] = dbSPL.toString().toFloat()
        rightProgress.progress = calibrationPOJO.calibrationRight.size
      }
    }
    calibrationPOJO.measuringLevel = levelInFloat
    calibrationPOJO.dbHl[frequency] = dbHl.toString().toFloat()
  }

  /**
   * Finish calibration listener
   */
  private fun finishCalibration() {
    val file: FileOutputStream = openFileOutput(CALIBRATION_FILE, Context.MODE_PRIVATE)

    try {
      val gson = Gson()
      val resultAsString = gson.toJson(calibrationPOJO)

      file.write(resultAsString.toByteArray())

      Toast.makeText(this, "File has been saved$filesDir", Toast.LENGTH_SHORT).show()
    } catch (e: FileNotFoundException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }

    file.close()
  }

  /**
   * Load default calibration from assets
   */
  private fun loadDefaultCalibration() {
    try {
      val file = assets.open("calibration.json")

      val leftStream = InputStreamReader(file)

      val resultsString = leftStream.buffered().use { it.readText() }

      val gson = Gson()

      calibrationPOJO = gson.fromJson(resultsString, Calibration::class.java)

      Toast.makeText(this, "Default calibration has been loaded", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(this, "Reading default calibration failed", Toast.LENGTH_SHORT).show()
    }
  }

  /**
   * Open result activity
   */
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun openResultActivity() {
    val resultIntent = Intent(this, ResultsActivity::class.java)
    resultIntent.putExtra("results", setupWave.getResult())
    startActivity(resultIntent)


    val sharedRef = getSharedPreferences("share", Context.MODE_PRIVATE) ?: return
    val gson = Gson()
    val objAsString = gson.toJson(setupWave.getResult())

    with(sharedRef.edit()) {
      putInt("pos", setupWave.currentIndex)
      putString("result", objAsString)
      putBoolean("side", setupWave.LEFT_CHANNEL)
      putBoolean("isDefaultCalibration", isDefaultCalibration!!)
      putBoolean("isCalibration", isCalibration!!)
      putInt("leftProgress", setupWave.getResult().resultsLeft.size)
      putInt("rightProgress", setupWave.getResult().resultsRight.size)
      putBoolean("wasSet", true)
      apply()
    }
  }
}