package com.igor_shaula.api_polling.ui_layer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.igor_shaula.api_polling.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: SharedViewModel by viewModels()

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Timber.e("caught exception: $exception\n\ton thread $thread")
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.timeToShowStubDataProposal.observe(this) { show ->
            if (show) {
                if (alertDialog?.isShowing == true) { // this shouldn't ever happen, but anyway
                    alertDialog?.dismiss()
                }
                alertDialog = createAlertDialogForProvidingWithStubData {
                    viewModel.onReadyToUseStubData()
                }
                alertDialog?.show()
            } else {
                alertDialog?.dismiss()
                alertDialog = null
            }
        }
    }

    override fun onStop() {
        super.onStop()
        alertDialog?.dismiss()
        alertDialog = null
    }

    private fun createAlertDialogForProvidingWithStubData(onReadyToUseStubData: () -> Unit): AlertDialog =
        AlertDialog.Builder(this)
            .setTitle(R.string.stubDataProposalTitle)
            .setMessage(R.string.stubDataProposalMessage)
            .setPositiveButton(R.string.stubDataProposalPositiveButtonText) { thisDialog, _ ->
                onReadyToUseStubData.invoke() // invoke() instead of () - just to be more visible
                thisDialog.cancel()
            }
            .setNegativeButton(R.string.stubDataProposalNegativeButtonText) { thisDialog, _ ->
                thisDialog.cancel()
            }
            .setOnDismissListener {
                Timber.v("AlertDialog is dismissed")
            }
            .setOnCancelListener {
                Timber.v("AlertDialog is cancelled")
            }
            .create()
}
