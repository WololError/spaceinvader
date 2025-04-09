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
            .setTitle(R.string.welcome_title)
            .setMessage(R.string.rules_text)
            .setPositiveButton("Normal Mode") { _, _ ->
                onStartNormalMode()
            }
            .setNegativeButton("Endless Mode") { _, _ ->
                onStartEndlessMode()
            }
            .create()
    }
}
