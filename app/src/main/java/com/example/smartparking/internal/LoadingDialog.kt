package com.example.smartparking.internal

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.smartparking.R


class LoadingDialog(val context: Context) {
    private val dialog = Dialog(context)

    fun startLoading(){
        dialog.setContentView(R.layout.loading_item)
        dialog.setCancelable(false)
        dialog.show()

    }
    fun dismiss(){
        dialog.dismiss()
    }
}