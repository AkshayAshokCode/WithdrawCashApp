package com.akshayashokcode.withdrawcashapp.ui.withdrawrequest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akshayashokcode.withdrawcashapp.R
import com.akshayashokcode.withdrawcashapp.data.model.WithdrawRequest
import com.akshayashokcode.withdrawcashapp.ui.MainActivity


class WithdrawRequestFragment : Fragment(), AmountBottomSheetFragment.OnSubmitListener {

    private val viewModel: WithdrawRequestViewModel by viewModels()

    private lateinit var amountEditText: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_withdraw_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        amountEditText = view.findViewById(R.id.amountEditText)
        val done: TextView = view.findViewById(R.id.doneButton)
        val availBalanceTv: TextView = view.findViewById(R.id.avail_balance)
        val prefix = getString(R.string.currency) + " "
        viewModel.availableBalance.observe(viewLifecycleOwner) { balance ->
            availBalanceTv.text = prefix + balance
        }
        setupToolbar(view)
        handleVideoLink(view)
        handleEditText(prefix)

        done.setOnClickListener {

            // Assuming that the user can withdraw at least KD 1.0
            if (viewModel.amount.value != null && viewModel.amount.value!! >= 1.0) {
                if (viewModel.amount.value!! > viewModel.availableBalance.value!!) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.amount_exceeds),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showWithdrawReceipt()
                }
            } else {
                if (viewModel.amount.value == null) {
                    Toast.makeText(requireContext(), getString(R.string.enter_amount), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.invalid_input), Toast.LENGTH_SHORT)
                        .show()
                }
                amountEditText.requestFocus()
                openKeyBoard()
            }
        }
    }

    private fun openKeyBoard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(amountEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showWithdrawReceipt() {
        val bottomSheetFragment = AmountBottomSheetFragment.newInstance(
            viewModel.amount.value ?: 0.0,
            viewModel.processingFee.value ?: 0.0,
            viewModel.totalAmount.value ?: 0.0,
        )
        bottomSheetFragment.setOnSubmitListener(this)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun handleEditText(prefix: String) {

        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isNotEmpty() && !text.startsWith(prefix)) {
                    val cursorPosition = amountEditText.selectionStart
                    amountEditText.setText("$prefix$text")
                    amountEditText.setSelection(cursorPosition + 3)
                }

                if (text == prefix) {
                    amountEditText.setText("")
                }

                val amountString = text.removePrefix(prefix).trim()
                val amount = amountString.toDoubleOrNull()
                if (amount != null) {
                    viewModel.onAmountChanged(amount)
                } else {
                    viewModel.onAmountChanged(null)
                }
                if (amount != null && amount > viewModel.availableBalance.value!!) {
                    amountEditText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                } else {
                    amountEditText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                }
            }
        })

    }

    @SuppressLint("ResourceType")
    private fun handleVideoLink(view: View) {
        val descriptionTextView: TextView = view.findViewById(R.id.watch_video)
        val text = getString(R.string.withdraw_help)

        val spannable = SpannableString(text)
        val start = text.indexOf(getString(R.string.watch_this_video))
        val end = start + getString(R.string.watch_this_video).length
//        ForegroundColorSpan(
//            ContextCompat.getColor(
//                requireContext(),
//                R.color.blue
//            )
//        )
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.blue)))
            , start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                // open video link
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        descriptionTextView.text = spannable
        descriptionTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onSubmit(total: Double) {
        // dummy withdraw request
        val withdrawRequest = WithdrawRequest(
            "1234",
            "67890",
            "withdrawal",
            total,
            "857433348",
            "KD"

        )

        (activity as? MainActivity)?.navigateToWithdrawConfirmationFragment(withdrawRequest)
    }

    private fun setupToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }
}