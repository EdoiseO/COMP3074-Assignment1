package ca.gbc.comp3074.onofua_phillip.assignment1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var hoursInput: EditText
    private lateinit var rateInput: EditText
    private lateinit var taxInput: EditText
    private val currency = NumberFormat.getCurrencyInstance(Locale.CANADA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hoursInput = findViewById(R.id.hours_input)
        rateInput = findViewById(R.id.rate_input)
        taxInput = findViewById(R.id.tax_input)
        if (savedInstanceState == null) showPlaceholders()
        findViewById<Button>(R.id.calculate_button).setOnClickListener { calculatePay() }
        taxInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculatePay()
                true
            } else false
        }
        findViewById<Button>(R.id.about_button).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun showPlaceholders() {
        val placeholder = getString(R.string.result_placeholder)
        showResult(R.id.pay_output, R.string.regular_result, placeholder)
        showResult(R.id.overtime_output, R.string.overtime_result, placeholder)
        showResult(R.id.total_output, R.string.total_result, placeholder)
        showResult(R.id.tax_output, R.string.tax_result, placeholder)
    }

    private fun readNumber(input: EditText, isTax: Boolean = false): BigDecimal? {
        input.error = null
        val text = input.text.toString().trim()
        val number = if (text.matches(Regex("(?:[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+)"))) text.toBigDecimalOrNull() else null
        if (number == null || number.signum() < 0 || (isTax && number > BigDecimal.ONE)) {
            input.error = getString(if (isTax) R.string.error_tax else R.string.error_number)
            return null
        }
        return number
    }

    private fun calculatePay() {
        val hours = readNumber(hoursInput)
        val rate = readNumber(rateInput)
        val tax = readNumber(taxInput, isTax = true)
        if (hours == null || rate == null || tax == null) {
            showPlaceholders()
            listOf(hoursInput, rateInput, taxInput).first { it.error != null }.requestFocus()
            return
        }
        val result = PayrollCalculator.calculate(hours, rate, tax)
        showResult(R.id.pay_output, R.string.regular_result, currency.format(result.pay))
        showResult(R.id.overtime_output, R.string.overtime_result, currency.format(result.overtimePay))
        showResult(R.id.total_output, R.string.total_result, currency.format(result.totalPay))
        showResult(R.id.tax_output, R.string.tax_result, currency.format(result.tax))
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(taxInput.windowToken, 0)
    }

    private fun showResult(viewId: Int, stringId: Int, value: String) {
        findViewById<TextView>(viewId).text = getString(stringId, value)
    }
}
