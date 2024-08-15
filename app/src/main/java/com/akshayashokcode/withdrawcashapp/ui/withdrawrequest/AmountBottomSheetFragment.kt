package com.akshayashokcode.withdrawcashapp.ui.withdrawrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.akshayashokcode.withdrawcashapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AmountBottomSheetFragment : BottomSheetDialogFragment() {

    private var onSubmitListener: OnSubmitListener? = null

    interface OnSubmitListener {
        fun onSubmit(total: Double)
    }

    companion object {
        fun newInstance(
            amount: Double,
            processingFee: Double,
            total: Double
        ): AmountBottomSheetFragment {
            val fragment = AmountBottomSheetFragment()
            val args = Bundle().apply {
                putDouble("amount", amount)
                putDouble("processingFee", processingFee)
                putDouble("total", total)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_amount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val amount = arguments?.getDouble("amount") ?: 0.0
        val processingFee = arguments?.getDouble("processingFee") ?: 0.0
        val totalAmount = arguments?.getDouble("total") ?: 0.0
        // Set the amount text
        val prefix = getString(R.string.currency) + " "
        view.findViewById<TextView>(R.id.totalAmountValue).text =
            "$prefix${amount}"
        view.findViewById<TextView>(R.id.processingFeeValue).text =
            "$prefix${processingFee}"
        view.findViewById<TextView>(R.id.totalValue).text =
            "$prefix${totalAmount}"

        view.findViewById<Button>(R.id.submitButton).setOnClickListener {
            onSubmitListener?.onSubmit(totalAmount)
            dismiss() // Close the bottom sheet after submitting
        }

    }


    fun setOnSubmitListener(listener: OnSubmitListener) {
        onSubmitListener = listener
    }
}