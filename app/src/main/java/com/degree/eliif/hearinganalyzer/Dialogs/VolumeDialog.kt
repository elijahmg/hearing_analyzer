package com.degree.eliif.hearinganalyzer.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class VolumeDialog : AppCompatDialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = AlertDialog.Builder(activity)

    dialog.setTitle("Volume alert")
      .setMessage("Do not change the volume on your device, use +5db /-5 dB to operate signal volume")
      .setPositiveButton("Ok" ){ dialog, which ->  }
    return dialog.create()
  }
}