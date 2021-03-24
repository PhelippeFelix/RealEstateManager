package com.openclassrooms.realestatemanager.Controller.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView

import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_loan.*


class LoanFragment : Fragment() {

    companion object {
        fun newInstance() : LoanFragment{
            return  LoanFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setOnClickListener()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun setOnClickListener(){
        loan_term.setOnClickListener{displayPopupMenu()}
        loan_calculate.setOnClickListener { calculate() }
    }

    private fun displayPopupMenu(){
        val popupMenu = PopupMenu(this.context, loan_term)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_loan_term, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item -> loan_term.setText(item.title); true}
        popupMenu.show()
    }

    // ---------------------
    // ACTION
    // ---------------------

    private fun calculate(){
        var canCalculate: Boolean
        val amount = loan_amount.text.toString().toDoubleOrNull() ?: 0.0
        val downPayment = loan_down.text.toString().toDoubleOrNull() ?: 0.0
        val term = loan_term.text.toString().toDoubleOrNull()
        val interest = loan_interest.text.toString().toDoubleOrNull()

        when{
            loan_amount.text.isNullOrEmpty() || loan_term.text.isNullOrEmpty() || loan_interest.text.isNullOrEmpty() || downPayment >= amount -> {
                canCalculate = false
                if (loan_amount.text.isNullOrEmpty()){
                    loan_amount_layout.error = resources.getString(R.string.loan_error)
                }
                if (loan_term.text.isNullOrEmpty()){
                    loan_term_layout.error = resources.getString(R.string.loan_error)
                }
                if (loan_interest.text.isNullOrEmpty()){
                    loan_interest_layout.error = resources.getString(R.string.loan_error)
                }else if (interest!! < 0 || interest > 100){
                    loan_interest_layout.error = resources.getString(R.string.loan_error_interest_value)
                }
                if (downPayment >= amount){
                    loan_down_layout.error = resources.getString(R.string.loan_error_down_payment)
                }
            }
            else -> {
                canCalculate = true
                loan_amount_layout.error = null
                loan_term_layout.error = null
                loan_interest_layout.error = null
                loan_down_layout.error = null
            }
        }

        if (canCalculate){
            val result: Double
            val totalPrice: Double
            if(interest == 0.0){
                result = (amount - (downPayment)) / (term!! * 12)
                totalPrice = 0.0
            }else{
                result = (amount - (downPayment)) * ((interest!! / (100)) / (12)) / (1 - Math.pow( 1 + ((interest / 100) / 12), -term!! *12))
                totalPrice = 12 * term * result - (amount.minus(downPayment))
            }
            loan_monthly.setText(String.format("%.2f",result),TextView.BufferType.EDITABLE)
            loan_total.setText(String.format("%.2f",totalPrice), TextView.BufferType.EDITABLE)

        }
    }
}
