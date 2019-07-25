package com.degree.eliif.hearinganalyzer.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class VolumeDialog : AppCompatDialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = AlertDialog.Builder(activity)

    dialog.setTitle("Volume alert")
      .setMessage("Do not change the volume on your device, use +5 dB HL /-5 dB HL to operate signal volume instead")
      .setPositiveButton("Ok" ){ _, _ ->  }
    return dialog.create()
  }
}