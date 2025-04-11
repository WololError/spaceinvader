package com.example.spaceinvader

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class StartDialog(
    private val onStartNormalMode: () -> Unit,
    private val onStartEndlessMode: () -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.welcome_title))
            .setMessage(
                getString(R.string.rules_text) +  "\n\n" +
                        getString(R.string.Rouge) +
                        getString(R.string.Mauve) +
                        getString(R.string.Rose)
            )

            .setPositiveButton(getString(R.string.Normal)) { _, _ ->
                onStartNormalMode()
            }
            .setNegativeButton(getString(R.string.endless)) { _, _ ->
                onStartEndlessMode()
            }
            .create()
    }

}
