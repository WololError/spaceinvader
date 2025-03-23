package com.example.spaceinvader

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class GameOverDialog(
    private val message: String,
    private val onRestart: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(message)
            .setPositiveButton(requireContext().getString(R.string.reset_game)) { _, _ ->
            onRestart()
            }
            .setCancelable(false)
            .create()
    }
}