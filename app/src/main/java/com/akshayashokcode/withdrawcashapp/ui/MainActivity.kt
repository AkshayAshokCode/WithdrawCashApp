package com.akshayashokcode.withdrawcashapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akshayashokcode.withdrawcashapp.R
import com.akshayashokcode.withdrawcashapp.data.model.WithdrawRequest
import com.akshayashokcode.withdrawcashapp.ui.withdrawconfirmation.WithdrawConfirmationFragment
import com.akshayashokcode.withdrawcashapp.ui.withdrawrequest.WithdrawRequestFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(WithdrawRequestFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateToWithdrawConfirmationFragment(withdrawRequest: WithdrawRequest) {
        val fragment = WithdrawConfirmationFragment().apply {
            arguments = Bundle().apply {
                putParcelable("withdrawRequest", withdrawRequest)
            }
        }
        loadFragment(fragment)
    }
}