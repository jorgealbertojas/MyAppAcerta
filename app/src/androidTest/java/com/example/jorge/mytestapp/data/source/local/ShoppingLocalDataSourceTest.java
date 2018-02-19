
package com.example.jorge.mytestapp.data.source.local;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;


import com.example.jorge.mytestapp.custom.action.util.SingleExecutors;
import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.ShoppingDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShoppingLocalDataSourceTest {

    private final static String PRODUCT_ID_1 = "100";
    private final static String PRODUCT_ID_2 = "200";
    private final static String PRODUCT_ID_3 = "300";

    private ShoppingLocalDataSource mLocalDataSource;

    private ToDoDatabase mDatabase;

    @Before
    public void setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                ToDoDatabase.class)
                .build();
        ShoppingDao tasksDao = mDatabase.ShoppingDao();

        // Make sure that we're not keeping a reference to the wrong instance.
        ShoppingLocalDataSource.clearInstance();
        mLocalDataSource = ShoppingLocalDataSource.getInstance(new SingleExecutors(), tasksDao);
    }

    @After
    public void cleanUp() {
        mLocalDataSource.deleteAllShopping();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void savePurchase_retrievesPurchase() {
        // Given a new Purchase
        final Purchase newPurchase = new Purchase(PRODUCT_ID_1, "","","","");

        // When saved into the persistent repository
        mLocalDataSource.savePurchase(newPurchase);

        // Then the Purchase can be retrieved from the persistent repository
        mLocalDataSource.getPurchase(newPurchase.getId(), new ShoppingDataSource.GetPurchaseCallback() {
            @Override
            public void onPurchaseLoaded(Purchase Purchase) {
                assertThat(Purchase, is(newPurchase));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void completePurchase_retrievedPurchaseIsComplete() {
        // Initialize mock for the callback.
        ShoppingDataSource.GetPurchaseCallback callback = mock(ShoppingDataSource.GetPurchaseCallback.class);
        // Given a new Purchase in the persistent repository
        final Purchase newPurchase = new Purchase(PRODUCT_ID_1, "","","","");
        mLocalDataSource.savePurchase(newPurchase);

        // When completed in the persistent repository
        mLocalDataSource.activatePurchase(newPurchase,"1");

        // Then the Purchase can be retrieved from the persistent repository and is complete
        mLocalDataSource.getPurchase(newPurchase.getId(), new ShoppingDataSource.GetPurchaseCallback() {
            @Override
            public void onPurchaseLoaded(Purchase Purchase) {
                assertThat(Purchase, is(newPurchase));
                assertThat(Purchase.isCompleted(), is(true));
            }

            @Override
            public void onDataNotAvailable() {
                fail("Callback error");
            }
        });
    }

    @Test
    public void activatePurchase_retrievedPurchaseIsActive() {
        // Initialize mock for the callback.
        ShoppingDataSource.GetPurchaseCallback callback = mock(ShoppingDataSource.GetPurchaseCallback.class);

        // Given a new completed Purchase in the persistent repository
        final Purchase newPurchase = new Purchase(PRODUCT_ID_1, "","","","");
        mLocalDataSource.savePurchase(newPurchase);
        mLocalDataSource.activatePurchase(newPurchase,"1");

        // When activated in the persistent repository
        mLocalDataSource.activatePurchase(newPurchase,"2");

        // Then the Purchase can be retrieved from the persistent repository and is active
        mLocalDataSource.getPurchase(newPurchase.getId(), callback);

        verify(callback, never()).onDataNotAvailable();
        verify(callback).onPurchaseLoaded(newPurchase);

        assertThat(newPurchase.isCompleted(), is(false));
    }

    @Test
    public void clearCompletedPurchase_PurchaseNotRetrievable() {
        // Initialize mocks for the callbacks.
        ShoppingDataSource.GetPurchaseCallback callback1 = mock(ShoppingDataSource.GetPurchaseCallback.class);
        ShoppingDataSource.GetPurchaseCallback callback2 = mock(ShoppingDataSource.GetPurchaseCallback.class);
        ShoppingDataSource.GetPurchaseCallback callback3 = mock(ShoppingDataSource.GetPurchaseCallback.class);

        // Given 2 new completed Shopping and 1 active Purchase in the persistent repository
        final Purchase newPurchase1 = new Purchase(PRODUCT_ID_1, "1","2","3","4");
        mLocalDataSource.savePurchase(newPurchase1);
        mLocalDataSource.activatePurchase(newPurchase1,"1");
        final Purchase newPurchase2 = new Purchase(PRODUCT_ID_2,"1","2","3","4");
        mLocalDataSource.savePurchase(newPurchase2);
        mLocalDataSource.savePurchase(newPurchase2);
        final Purchase newPurchase3 = new Purchase(PRODUCT_ID_3, "5","6","","7");
        mLocalDataSource.savePurchase(newPurchase3);

        // When completed Shopping are cleared in the repository
        //mLocalDataSource.clearCompletedShopping();

        // Then the completed Shopping cannot be retrieved and the active one can
        mLocalDataSource.getPurchase(newPurchase1.getId(), callback1);

        verify(callback1).onDataNotAvailable();
        verify(callback1, never()).onPurchaseLoaded(newPurchase1);

        mLocalDataSource.getPurchase(newPurchase2.getId(), callback2);

        verify(callback2).onDataNotAvailable();
        verify(callback2, never()).onPurchaseLoaded(newPurchase2);

        mLocalDataSource.getPurchase(newPurchase3.getId(), callback3);

        verify(callback3, never()).onDataNotAvailable();
        verify(callback3).onPurchaseLoaded(newPurchase3);
    }

    @Test
    public void deleteAllShopping_emptyListOfRetrievedPurchase() {
        // Given a new Purchase in the persistent repository and a mocked callback
        Purchase newPurchase = new Purchase(PRODUCT_ID_1, "","","","");
        mLocalDataSource.savePurchase(newPurchase);
        ShoppingDataSource.LoadShoppingCallback callback = mock(ShoppingDataSource.LoadShoppingCallback.class);

        // When all Shopping are deleted
        mLocalDataSource.deleteAllShopping();

        // Then the retrieved Shopping is an empty list
        mLocalDataSource.getShopping(callback);

        verify(callback).onDataNotAvailable();
        verify(callback, never()).onShoppingLoaded(anyList());
    }

    @Test
    public void getShopping_retrieveSavedShopping() {
        // Given 2 new Shopping in the persistent repository
        final Purchase newPurchase1 = new Purchase(PRODUCT_ID_1, "","","","");
        mLocalDataSource.savePurchase(newPurchase1);
        final Purchase newPurchase2 = new Purchase(PRODUCT_ID_1, "","","","");
        mLocalDataSource.savePurchase(newPurchase2);

        // Then the Shopping can be retrieved from the persistent repository
        mLocalDataSource.getShopping(new ShoppingDataSource.LoadShoppingCallback() {
            @Override
            public void onShoppingLoaded(List<Purchase> Shopping) {
                assertNotNull(Shopping);
                assertTrue(Shopping.size() >= 2);

                boolean newPurchase1IdFound = false;
                boolean newPurchase2IdFound = false;
                for (Purchase Purchase : Shopping) {
                    if (Purchase.getId().equals(newPurchase1.getId())) {
                        newPurchase1IdFound = true;
                    }
                    if (Purchase.getId().equals(newPurchase2.getId())) {
                        newPurchase2IdFound = true;
                    }
                }
                assertTrue(newPurchase1IdFound);
                assertTrue(newPurchase2IdFound);
            }

            @Override
            public void onDataNotAvailable() {
                fail();
            }
        });
    }
}
