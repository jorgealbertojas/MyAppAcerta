
package com.example.jorge.mytestapp.shopping;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;


import com.example.jorge.mytestapp.Injection;
import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.jorge.mytestapp.TestUtils.getCurrentActivity;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests for the Shopping screen, the main screen which contains a list of all Shopping.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShoppingScreenTest {

    private final static String PRODUCT_ID1 = "PRODUCT_ID1";

    private final static String QUANTITY = "1";

    private final static String PRODUCT_ID2 = "PRODUCT_ID2";


    @Rule
    public ActivityTestRule<ShoppingActivity> mShoppingActivityTestRule =
            new ActivityTestRule<ShoppingActivity>(ShoppingActivity.class) {


                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    // Doing this in @Before generates a race condition.
                    Injection.provideShoppingRepository(InstrumentationRegistry.getTargetContext())
                        .deleteAllShopping();
                }
            };

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(ListView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA LV with text " + itemText);
            }
        };
    }

    @Test
    public void clickAddTaskButton_opensAddTaskUi() {
        // Click on the add task button
        onView(withId(R.id.fab_add_purchase)).perform(click());

    }

    @Test
    public void editTask() throws Exception {
        // First add a purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Click on the purchase on the list
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click on the edit purchase button
        onView(withId(R.id.fab_edit_purchase)).perform(click());

        String editTaskTitle = PRODUCT_ID2;
        String editTaskDescription = "New Description";


        onView(withId(R.id.tv_quantity)).perform(replaceText(editTaskDescription),
                closeSoftKeyboard()); // Type new purchase description and close the keyboard

        // Save the purchase
        onView(withId(R.id.fab_edit_purchase_done)).perform(click());

        // Verify purchase is displayed on screen in the purchase list.
        onView(withItemText(editTaskTitle)).check(matches(isDisplayed()));

        // Verify previous purchase is not displayed
        onView(withItemText(PRODUCT_ID1)).check(doesNotExist());
    }

    @Test
    public void addTaskToShoppingList() throws Exception {
        createTask(PRODUCT_ID1, QUANTITY);

        // Verify purchase is displayed on screen
        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));
    }

    @Test
    public void markTaskAsComplete() {


        // Add active purchase
        createTask(PRODUCT_ID1, QUANTITY);


        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));

        onView(withItemText(PRODUCT_ID1)).check(matches(not(isDisplayed())));

        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));
    }

    @Test
    public void markTaskAsActive() {

        // Add completed purchase
        createTask(PRODUCT_ID1, QUANTITY);

        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));

        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));

        onView(withItemText(PRODUCT_ID1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void showAllShopping() {
        // Add 2 active Shopping
        createTask(PRODUCT_ID1, QUANTITY);
        createTask(PRODUCT_ID2, QUANTITY);


        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));
        onView(withItemText(PRODUCT_ID2)).check(matches(isDisplayed()));
    }

    @Test
    public void showActiveShopping() {
        // Add 2 active Shopping
        createTask(PRODUCT_ID1, QUANTITY);
        createTask(PRODUCT_ID2, QUANTITY);

        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));
        onView(withItemText(PRODUCT_ID2)).check(matches(isDisplayed()));
    }

    @Test
    public void showCompletedShopping() {
        // Add 2 completed Shopping
        createTask(PRODUCT_ID1, QUANTITY);
        createTask(PRODUCT_ID2, QUANTITY);

        // Verify that all our Shopping are shown

        onView(withItemText(PRODUCT_ID1)).check(matches(isDisplayed()));
        onView(withItemText(PRODUCT_ID2)).check(matches(isDisplayed()));
    }

    @Test
    public void clearCompletedShopping() {

        // Add 2 complete Shopping
        createTask(PRODUCT_ID1, QUANTITY);
        createTask(PRODUCT_ID2, QUANTITY);

        // Click clear completed in menu
        openActionBarOverflowOrOptionsMenu(getTargetContext());

        //Verify that completed Shopping are not shown
        onView(withItemText(PRODUCT_ID1)).check(matches(not(isDisplayed())));
        onView(withItemText(PRODUCT_ID2)).check(matches(not(isDisplayed())));
    }

    @Test
    public void createOneTask_deleteTask() {


        // Add active purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Open it in details view
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click delete purchase in menu
        onView(withId(R.id.menu_delete)).perform(click());

        // Verify it was deleted

        onView(withText(PRODUCT_ID1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void createTwoShopping_deleteOneTask() {
        // Add 2 active Shopping
        createTask(PRODUCT_ID1, QUANTITY);
        createTask(PRODUCT_ID2, QUANTITY);

        // Open the second purchase in details view
        onView(withText(PRODUCT_ID2)).perform(click());

        // Click delete purchase in menu
        onView(withId(R.id.menu_delete)).perform(click());

        // Verify only one purchase was deleted

        onView(withText(PRODUCT_ID1)).check(matches(isDisplayed()));
        onView(withText(PRODUCT_ID2)).check(doesNotExist());
    }

    @Test
    public void markTaskAsCompleteOnDetailScreen_purchaseIsCompleteInList() {


        // Add 1 active purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Click on the purchase on the list
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click());

    }

    @Test
    public void markTaskAsActiveOnDetailScreen_purchaseIsActiveInList() {


        // Add 1 completed purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Click on the purchase on the list
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click());

    }

    @Test
    public void markTaskAsAcompleteAndActiveOnDetailScreen_purchaseIsActiveInList() {


        // Add 1 active purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Click on the purchase on the list
        onView(withText(PRODUCT_ID1)).perform(click());


        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click());

      }

    @Test
    public void markTaskAsActiveAndCompleteOnDetailScreen_purchaseIsCompleteInList() {


        // Add 1 completed purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Click on the purchase on the list
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click on the navigation up button to go back to the list
        onView(withContentDescription(getToolbarNavigationContentDescription())).perform(click());


    }

    @Test
    public void orientationChange_FilterActivePersists() {

        // Add a completed purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // then no Shopping should appear
        onView(withText(PRODUCT_ID1)).check(matches(not(isDisplayed())));

        // when rotating the screen
        TestUtils.rotateOrientation(mShoppingActivityTestRule.getActivity());

        // then nothing changes
        onView(withText(PRODUCT_ID1)).check(doesNotExist());
    }

    @Test
    public void orientationChange_FilterCompletedPersists() {

        // Add a completed purchase
        createTask(PRODUCT_ID1, QUANTITY);
        // the completed purchase should be displayed
        onView(withText(PRODUCT_ID1)).check(matches(isDisplayed()));

        // when rotating the screen
        TestUtils.rotateOrientation(mShoppingActivityTestRule.getActivity());

        // then nothing changes
        onView(withText(PRODUCT_ID1)).check(matches(isDisplayed()));

    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    public void orientationChange_DuringEdit_ChangePersists() throws Throwable {
        // Add a completed purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Open the purchase in details view
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click on the edit purchase button
        onView(withId(R.id.fab_edit_purchase)).perform(click());


        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    public void orientationChange_DuringEdit_NoDuplicate() throws IllegalStateException {
        // Add a completed purchase
        createTask(PRODUCT_ID1, QUANTITY);

        // Open the purchase in details view
        onView(withText(PRODUCT_ID1)).perform(click());

        // Click on the edit purchase button
        onView(withId(R.id.fab_edit_purchase)).perform(click());

        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        // Edit purchase title and description

        onView(withId(R.id.tv_quantity)).perform(replaceText(QUANTITY),
                closeSoftKeyboard()); // Type new purchase description and close the keyboard

        // Save the purchase
        onView(withId(R.id.fab_edit_purchase_done)).perform(click());

        // Verify purchase is displayed on screen in the purchase list.
        onView(withItemText(PRODUCT_ID2)).check(matches(isDisplayed()));

        // Verify previous purchase is not displayed
        onView(withItemText(PRODUCT_ID1)).check(doesNotExist());
    }



    private void createTask(String title, String description) {
        // Click on the add purchase button
        onView(withId(R.id.fab_add_purchase)).perform(click());


        onView(withId(R.id.tv_quantity)).perform(typeText(description),
                closeSoftKeyboard()); // Type new purchase description and close the keyboard

        // Save the purchase
        onView(withId(R.id.fab_edit_purchase_done)).perform(click());
    }


    private String getText(int stringId) {
        return mShoppingActivityTestRule.getActivity().getResources().getString(stringId);
    }

    private String getToolbarNavigationContentDescription() {
        return TestUtils.getToolbarNavigationContentDescription(
                mShoppingActivityTestRule.getActivity(), R.id.toolbar);
    }
}
