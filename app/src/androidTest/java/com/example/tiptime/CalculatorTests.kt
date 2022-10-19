package com.example.tiptime

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorTests {

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun calculate_20_percent_tip() {
        onView(withId(R.id.cost_of_service_edit_text))
            .perform(typeText("50.00"))//Enter value into Cost of Service field
            .perform(ViewActions.closeSoftKeyboard())//close soft keyboard so calculate button is visible

        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button

        //Radio group for tip percent defaults to 20%
        //Radio group for rounding defaults to round total

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("$10.00")))) //verify tip field shows 20% of cost of service

        onView(withId(R.id.total_with_tip))
            .check(matches(withText(containsString("$60.00")))) //Verify total cost field shows tip+cost of service
        // TODO can I make these tests work other currency formats?
    }
    //TODO check functionality when other radio buttons are selected for tip percent and rounding

}
