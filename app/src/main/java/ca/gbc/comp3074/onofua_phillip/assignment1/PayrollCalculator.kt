package ca.gbc.comp3074.onofua_phillip.assignment1

import java.math.BigDecimal

data class PayrollResult(
    val pay: BigDecimal,
    val overtimePay: BigDecimal,
    val totalPay: BigDecimal,
    val tax: BigDecimal
)

object PayrollCalculator {
    fun calculate(hours: BigDecimal, hourlyRate: BigDecimal, taxRate: BigDecimal): PayrollResult {
        require(hours.signum() >= 0) { "Hours must not be negative" }
        require(hourlyRate.signum() >= 0) { "Hourly rate must not be negative" }
        require(taxRate >= BigDecimal.ZERO && taxRate <= BigDecimal.ONE) { "Tax rate must be between 0 and 1" }
        val regularHours = hours.min(BigDecimal("40"))
        val overtimeHours = (hours - regularHours)
        val pay = regularHours * hourlyRate
        val overtimePay = overtimeHours * hourlyRate * BigDecimal("1.5")
        // The assignment specifies tax on regular pay, excluding overtime.
        return PayrollResult(pay, overtimePay, pay + overtimePay, pay * taxRate)
    }
}
