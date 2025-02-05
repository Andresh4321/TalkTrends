package com.example.talktrends.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.example.talktrends.R

class LoadingUtils(val activity: Activity) {
    private var alertDialog: AlertDialog? = null

    fun show() {
        val builder = AlertDialog.Builder(activity)

        val dialogView: View = LayoutInflater.from(activity).inflate(R.layout.activity_edit_profile, null, false)

        builder.setView(dialogView)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog?.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}
