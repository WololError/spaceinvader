package com.example.spaceinvader

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class GameOverDialog(
    private val message: String,
    private val onRestart: () -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(message)
            .setPositiveButton(requireContext().getString(R.string.reset_game)) { _, _ ->
                onRestart()
            }
            .create()
    }
}