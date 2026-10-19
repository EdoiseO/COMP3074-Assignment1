package ca.gbc.comp3074.onofua_phillip.assignment1

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PayrollCalculatorTest {
    private fun calculate(hours: String, rate: String = "20", tax: String = "0.15") =
        PayrollCalculator.calculate(BigDecimal(hours), BigDecimal(rate), BigDecimal(tax))

    private fun money(expected: String, actual: BigDecimal) =
        assertEquals(0, BigDecimal(expected).compareTo(actual))

    @Test fun belowFortyHours() {
        val result = calculate("30")
        money("600", result.pay)
        money("0", result.overtimePay)
        money("600", result.totalPay)
        money("90", result.tax)
    }

    @Test fun exactlyFortyHoursHasNoOvertime() {
        val result = calculate("40")
        money("800", result.pay)
        money("0", result.overtimePay)
        money("800", result.totalPay)
    }

    @Test fun overtimeUsesTimeAndAHalfAndTaxExcludesOvertime() {
        val result = calculate("45")
        money("800", result.pay)
        money("150", result.overtimePay)
        money("950", result.totalPay)
        money("120", result.tax)
    }

    @Test fun decimalHoursAndRatesKeepPrecision() {
        val result = calculate("40.5", "22.50", "0.10")
        money("900", result.pay)
        money("16.875", result.overtimePay)
        money("916.875", result.totalPay)
        money("90", result.tax)
    }

    @Test fun zeroHoursAndZeroTax() {
        val result = calculate("0", tax = "0")
        money("0", result.totalPay)
        money("0", result.tax)
    }

    @Test fun rejectsNegativeInputsAndTaxOutsideRange() {
        assertThrows(IllegalArgumentException::class.java) { calculate("-1") }
        assertThrows(IllegalArgumentException::class.java) { calculate("40", "-1") }
        assertThrows(IllegalArgumentException::class.java) { calculate("40", tax = "-0.1") }
        assertThrows(IllegalArgumentException::class.java) { calculate("40", tax = "1.01") }
    }
}
