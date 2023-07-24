package com.endcodev.roll_dices.presenter.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.endcodev.roll_dices.data.model.ErrorModel
import com.endcodev.roll_dices.databinding.DialogFragmentErrorBinding
import kotlin.system.exitProcess

class ErrorDialogFragment(
    private val onAcceptClickLister: (Boolean) -> Unit,
    private val error: ErrorModel,
) : DialogFragment() {

    private var _binding: DialogFragmentErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            _binding = DialogFragmentErrorBinding.inflate(layoutInflater).apply {}

            onBackPress()

            initViews()

            initListeners()

            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    /**
     * Init button listeners
     */
    private fun initListeners(){
        binding.errorAccept.setOnClickListener {
            onAcceptClickLister.invoke(true)
            dismiss()
        }

        binding.errorCancel.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }
    }

    /**
     * Handle the onBackPressed event to do nothing when the back button is pressed
     */
    private fun onBackPress() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
    }


    /**
     * Initialize the views in the dialog with the error details
     */
    private fun initViews() {
        binding.errorTitle.text = error.title
        binding.errorDescription.text = error.description
        binding.errorAccept.text = error.acceptButton
        binding.errorCancel.text = error.cancelButton
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}