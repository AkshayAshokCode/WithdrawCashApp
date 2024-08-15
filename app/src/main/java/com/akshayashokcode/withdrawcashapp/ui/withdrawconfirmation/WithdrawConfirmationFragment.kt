package com.akshayashokcode.withdrawcashapp.ui.withdrawconfirmation

import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.MaskFilterSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akshayashokcode.withdrawcashapp.R
import com.akshayashokcode.withdrawcashapp.data.model.WithdrawRequest

class WithdrawConfirmationFragment : Fragment() {

    private var isBlurred = true
    private val blurRadius = 15f
    private val viewModel: WithdrawConfirmationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_withdraw_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val withdrawRequest: WithdrawRequest? = arguments?.getParcelable("withdrawRequest")
        val expireTimeTv: TextView = view.findViewById(R.id.expireTime)
        val digitBox1: TextView = view.findViewById(R.id.box1)
        val digitBox2: TextView = view.findViewById(R.id.box2)
        val digitBox3: TextView = view.findViewById(R.id.box3)
        val digitBox4: TextView = view.findViewById(R.id.box4)
        val showCodeButton: Button = view.findViewById(R.id.show_code_button)

        setupToolbar(view)

        handleVideoLink(view)

        viewModel.getConfirmationCode(withdrawRequest)
        viewModel.confirmationCode.observe(viewLifecycleOwner) { code ->
            setBlurredText(digitBox1, digitBox2, digitBox3, digitBox4, blurRadius, true, code)
        }

        viewModel.expirationTime.observe(viewLifecycleOwner) { (hours, minutes) ->
            val formattedExpireTime = getString(R.string.expire_time, hours, minutes)
            expireTimeTv.text = formattedExpireTime
        }

        showCodeButton.setOnClickListener {
            isBlurred = !isBlurred
            setBlurredText(
                digitBox1,
                digitBox2,
                digitBox3,
                digitBox4,
                blurRadius,
                isBlurred,
                viewModel.confirmationCode.value ?: ""
            )
            showCodeButton.text =
                if (isBlurred) getString(R.string.show_code) else getString(R.string.hide_code)
        }
    }

    private fun setBlurredText(
        digitBox1: TextView,
        digitBox2: TextView,
        digitBox3: TextView,
        digitBox4: TextView,
        blurRadius: Float,
        blur: Boolean,
        code: String
    ) {
        val blurredText = SpannableString(code)
        if (blur) {
            val blurMask = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            val maskFilterSpan = MaskFilterSpan(blurMask)
            blurredText.setSpan(maskFilterSpan, 0, code.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        digitBox1.text = if (blur) blurredText else code[0].toString()
        digitBox2.text = if (blur) blurredText else code[1].toString()
        digitBox3.text = if (blur) blurredText else code[2].toString()
        digitBox4.text = if (blur) blurredText else code[3].toString()
    }

    private fun handleVideoLink(view: View) {
        val descriptionTextView: TextView = view.findViewById(R.id.watch_video)
        val text = getString(R.string.withdraw_help)

        val spannable = SpannableString(text)
        val start = text.indexOf(getString(R.string.watch_this_video))
        val end = start + getString(R.string.watch_this_video).length

        spannable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue
                )
            ), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
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

    private fun setupToolbar(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}