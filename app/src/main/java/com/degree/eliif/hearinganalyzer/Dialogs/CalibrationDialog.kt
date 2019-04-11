package com.degree.eliif.hearinganalyzer.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class CalibrationDialog : AppCompatDialogFragment() {
  lateinit var listener: CalibrationDialogListener

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = AlertDialog.Builder(activity)

    dialog.setTitle("Choose calibration")
      .setMessage("Would you like to use custom or default calibration?")
      .setPositiveButton("Default"){ _, _ ->
        listener.setChoice(true)
      }
      .setNegativeButton("Custom"){ _, _ ->
        listener.setChoice(false)
      }

    return dialog.create()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    try {
      listener = context as CalibrationDialogListener
    } catch (e: ClassCastException) {
      throw java.lang.ClassCastException("Must implements")
    }
  }

  interface CalibrationDialogListener {
    fun setChoice(choice: Boolean)
  }
}