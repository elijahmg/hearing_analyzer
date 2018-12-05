package com.degree.eliif.hearinganalyzer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.degree.eliif.hearinganalyzer.R.id.textView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var setupWave: PlayWave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupWave = PlayWave()

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

        /** Create time spinner **/
        val timeSpinner: Spinner = this.findViewById(R.id.timeSpinner)

        ArrayAdapter.createFromResource(
                this,
                R.array.time_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeSpinner.adapter = adapter
        }

        timeSpinner.onItemSelectedListener = this

        }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent?.id == R.id.frequencySpinner) {
            val frequency = parent.getItemAtPosition(pos)
            textView!!.text = frequency.toString()

            setupWave.setWave(frequency.toString().toInt())
        }

        if (parent?.id == R.id.timeSpinner) {
            val time = parent.getItemAtPosition(pos)

            setupWave.setLoopLength(time.toString().toInt())
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onClick(view: View) {
        setupWave.stop()
        setupWave.play()
    }

}
