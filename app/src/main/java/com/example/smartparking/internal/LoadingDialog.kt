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
//
//class StartGameDialogFragment : DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//            builder.setMessage("asd")
//                .setPositiveButton("asd",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // START THE GAME!
//                    })
//                .setNegativeButton("osd",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
//}