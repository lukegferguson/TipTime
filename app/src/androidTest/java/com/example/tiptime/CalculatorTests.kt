package com.example.tiptime

import android.view.KeyEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorTests {

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun calculate_20_percent_tip_round_total() {
        onView(withId(R.id.cost_of_service_edit_text))
            .perform(typeText("53.26")) //Enter value into Cost of Service field
            .perform(ViewActions.closeSoftKeyboard())
            /* Close keyboard so calculate button is visible.
             Hitting enter key doesn't close keyboard fast enough when not also clicking radio buttons,
             so can't use KeyEvent.KEYCODE_ENTER like on the other tests
             */

        //Radio group for tip percent defaults to 20%
        //Radio group for rounding defaults to round total

        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("10.74")))) //verify tip field shows rounded amount to make total whole dollar

        onView(withId(R.id.total_with_tip))
            .check(matches(withText(containsString("64.00")))) //Verify total cost field shows cost of service + adjusted tip to make whole dollar
        // TODO can I make these tests work other currency formats?
    }

    @Test
    fun check_18_percent_tip_round_tip(){
        onView(withId(R.id.cost_of_service_edit_text))
            .perform(typeText("78.45"))//Enter value into Cost of Service field
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))//close soft keyboard so calculate button is visible and verify enter key listener works

        onView(withId(R.id.option_eighteen_percent)).perform(click()) //select 18% tip radio button
        onView(withId(R.id.round_tip)).perform(click()) //select round tip radio button
        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("15.00")))) //verify 18% tip is rounded up to next dollar
        onView(withId(R.id.total_with_tip))
            .check(matches(withText(containsString("93.45")))) //verify total cost is rounded tip + cost of service
    }

    @Test
    fun check_15_percent_tip_no_rounding(){
        onView(withId(R.id.cost_of_service_edit_text))
            .perform(typeText("113.26"))//Enter value into Cost of Service field
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))//close soft keyboard so calculate button is visible and verify enter key listener works

        onView(withId(R.id.option_fifteen_percent)).perform(click()) //select 15% tip radio button
        onView(withId(R.id.round_none)).perform(click()) //select no rounding radio button
        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("16.99")))) // verify 15% tip is shown with no rounding
        onView(withId(R.id.total_with_tip))
            .check(matches(withText(containsString("130.25")))) //verify total cost is tip+cost of service
    }

    @Test
    fun check_null_safety(){
        //nothing entered in cost of service to test null handling
        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("0.00")))) // verify 0 amount in tip for 0 cost of service
        onView(withId(R.id.total_with_tip))
            .check(matches(withText(containsString("0.00")))) //verify 0 in total with tip for 0 cost of service

    }

    @Test
    fun check_null_safety_after_use(){
        onView(withId(R.id.cost_of_service_edit_text))
            .perform(typeText("12.15"))//Enter value into Cost of Service field
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))//close soft keyboard so calculate button is visible and verify enter key listener works

        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button so that a result is displayed

        repeat(5) { //press delete key to clear cost of service
            onView(withId(R.id.cost_of_service_edit_text))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
        }
        onView(withId(R.id.cost_of_service_edit_text))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))//close soft keyboard so calculate button is visible and verify enter key listener works

        onView(withId(R.id.calculate_button)).perform(click())//click Calculate button

        onView(withId(R.id.tip_result))
            .check(matches(withText(containsString("0.00")))) // verify previous result is cleared and 0 is displayed
        onView(withId(R.id.total_with_tip))
            .check(matches(withText(containsString("0.00")))) //verify previous result is cleared and 0 is displayed

    }
}
