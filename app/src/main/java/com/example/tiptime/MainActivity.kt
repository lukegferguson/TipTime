package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding //lateinit makes compiler initialize this variable before using it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener { calculateTip() }

        //key listener to close keyboard when enter is pressed in cost of service edit text field
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }

    }

    private fun calculateTip() {
        //Take user input, convert to text, then to double.
        val cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull()
        if (cost == null) {
            displayAmounts(
                0.0,
                0.0
            ) //reset tip result field to 0 if null value is entered so that the previous result does not linger
            return //skip the rest of calculateTip() since it can't work on a null.
            //cost_of_service TextView is already set to inputType="numberDecimal" so don't need to worry about non-number input
        }
        //create variable for tip percentage based on radio button selected
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        var tip = tipPercentage * cost

        //built in math function for rounding up, awesome!
        val totalCost = when (binding.roundingOptions.checkedRadioButtonId){
            R.id.round_tip -> kotlin.math.ceil(tip) + cost
            R.id.round_total -> kotlin.math.ceil(cost+tip)
            else -> cost+tip
        }

        displayAmounts((totalCost-cost), totalCost)
    }

    //helper function to format the currency then update the string resources and pass in the formatted currency
    private fun displayAmounts(tip: Double, totalCost: Double) {
        //built in formatting for currency, can easily change display with language/country
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        val formattedCost = NumberFormat.getCurrencyInstance().format(totalCost)

        //display formatted tip and total cost to user
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        binding.totalWithTip.text = getString(R.string.total_with_tip, formattedCost)
    }

    private fun handleKeyEvent(
        view: View,
        keyCode: Int
    ): Boolean { // listener to close soft keyboard when enter key is pressed
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

}
