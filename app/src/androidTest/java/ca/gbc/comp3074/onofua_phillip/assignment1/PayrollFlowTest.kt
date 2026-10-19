package ca.gbc.comp3074.onofua_phillip.assignment1

import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PayrollFlowTest {
    private fun captureScreen(name: String) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        val image = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
        File(instrumentation.targetContext.cacheDir, name).outputStream().use {
            image.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        image.recycle()
    }

    private fun fillAndCalculate(hours: String = "45", rate: String = "20", tax: String = "0.15") {
        onView(withId(R.id.hours_input)).perform(scrollTo(), replaceText(hours), closeSoftKeyboard())
        onView(withId(R.id.rate_input)).perform(scrollTo(), replaceText(rate), closeSoftKeyboard())
        onView(withId(R.id.tax_input)).perform(scrollTo(), replaceText(tax), closeSoftKeyboard())
        onView(withId(R.id.calculate_button)).perform(scrollTo(), click())
    }

    @Test fun calculationAndBothAboutReturnPathsPreserveMainInstance() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            var original: MainActivity? = null
            scenario.onActivity { original = it }
            fillAndCalculate()
            onView(withId(R.id.pay_output)).check(matches(withText("Pay: $800.00")))
            onView(withId(R.id.overtime_output)).check(matches(withText("Overtime pay: $150.00")))
            onView(withId(R.id.total_output)).check(matches(withText("Total pay: $950.00")))
            onView(withId(R.id.tax_output)).check(matches(withText("Tax: $120.00")))
            captureScreen("payroll-main.png")
            onView(withId(R.id.about_button)).perform(scrollTo(), click())
            onView(withId(R.id.student_name)).check(matches(withText("Phillip Onofua")))
            onView(withId(R.id.student_id)).check(matches(withText("Student ID: 101489064")))
            captureScreen("payroll-about.png")
            onView(withContentDescription("Navigate up")).perform(click())
            scenario.onActivity { assertSame(original, it) }
            onView(withId(R.id.hours_input)).check(matches(withText("45")))
            onView(withId(R.id.total_output)).check(matches(withText("Total pay: $950.00")))
            onView(withId(R.id.about_button)).perform(scrollTo(), click())
            pressBack()
            scenario.onActivity { assertSame(original, it) }
            onView(withId(R.id.tax_input)).check(matches(withText("0.15")))
        }
    }

    @Test fun valuesAndResultsSurviveActivityRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            fillAndCalculate("40", "20", "0.10")
            scenario.recreate()
            onView(withId(R.id.hours_input)).check(matches(withText("40")))
            onView(withId(R.id.overtime_output)).check(matches(withText("Overtime pay: $0.00")))
            onView(withId(R.id.total_output)).check(matches(withText("Total pay: $800.00")))
            onView(withId(R.id.tax_output)).check(matches(withText("Tax: $80.00")))
        }
    }

    @Test fun blankAndOutOfRangeInputsShowErrorsAndClearOldResults() {
        ActivityScenario.launch(MainActivity::class.java).use {
            fillAndCalculate()
            fillAndCalculate(hours = "", tax = "15")
            onView(withId(R.id.hours_input)).check(matches(hasErrorText("Enter a valid number (zero or greater).")))
            onView(withId(R.id.tax_input)).check(matches(hasErrorText("Enter a decimal between 0 and 1, such as 0.15.")))
            onView(withId(R.id.total_output)).check(matches(withText("Total pay: —")))
            fillAndCalculate("0", "0", "0")
            onView(withId(R.id.total_output)).check(matches(withText("Total pay: $0.00")))
        }
    }
}
