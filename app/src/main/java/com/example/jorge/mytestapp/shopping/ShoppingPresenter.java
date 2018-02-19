package com.example.jorge.mytestapp.shopping;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.jorge.mytestapp.addPurchase.AddPurchaseActivity;
import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.ShoppingDataSource;
import com.example.jorge.mytestapp.data.source.ShoppingRepository;
import com.example.jorge.mytestapp.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 15/02/2018.
 */

public class ShoppingPresenter implements ShoppingContract.Presenter {

    private final ShoppingRepository mShoppingRepository;

    private final ShoppingContract.View mShoppingView;


    private boolean mFirstLoad = true;

    public ShoppingPresenter(@NonNull ShoppingRepository shoppingRepository, @NonNull ShoppingContract.View shoppingView) {
        mShoppingRepository = checkNotNull(shoppingRepository, "tasksRepository cannot be null");
        mShoppingView = checkNotNull(shoppingView, "tasksView cannot be null!");

        mShoppingView.setPresenter(this);
    }

    @Override
    public void start() {
        loadShopping(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackBar
        if (AddPurchaseActivity.REQUEST_ADD_PURCHASE == requestCode && Activity.RESULT_OK == resultCode) {
            mShoppingView.showSuccessfullySavedMessage();
        }
    }


    @Override
    public void loadShopping(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadShopping(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }




    private void loadShopping(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
        //    mShoppingView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
        //    mShoppingRepository.refreshShopping();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mShoppingRepository.getShopping(new ShoppingDataSource.LoadShoppingCallback() {

            @Override
            public void onShoppingLoaded(List<Purchase> purchaseList) {
                List<Purchase> tasksToShow = new ArrayList<Purchase>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (Purchase purchase : purchaseList) {
                    tasksToShow.add(purchase);
                }

                // The view may not be able to handle UI updates anymore
                if (!mShoppingView.isActive()) {
                    return;
                }
                //if (showLoadingUI) {
                if (true) {
                    mShoppingView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mShoppingView.isActive()) {
                    return;
                }
                mShoppingView.showLoadingShoppingError();
            }
        });
    }



    @Override
    public void FindShopping(String partName) {
        findShopping(partName);
    }

    private void findShopping(String partName) {
        if (true) {
     //       mShoppingView.setLoadingIndicator(true);
        }
        if (true) {
      //      mShoppingRepository.refreshShopping();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mShoppingRepository.getFind(new ShoppingDataSource.FindShoppingCallback() {



            @Override
            public void onFindLoaded(List<Purchase> purchaseList) {
                List<Purchase> tasksToShow = new ArrayList<Purchase>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (Purchase purchase : purchaseList) {
                    tasksToShow.add(purchase);
                }

                // The view may not be able to handle UI updates anymore
                if (!mShoppingView.isActive()) {
                    return;
                }
                if (true) {
                    mShoppingView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mShoppingView.isActive()) {
                    return;
                }
                mShoppingView.showLoadingShoppingError();
            }
        },partName);
    }


    @Override
    public void addNewPurchase() {
        mShoppingView.showAddPurchase();
    }

    @Override
    public void openPurchaseDetails(@NonNull Purchase requestedPurchase) {
        checkNotNull(requestedPurchase, "requestedTask cannot be null!");
        mShoppingView.showPurchaseDetailsUi(requestedPurchase.getId());
    }

    @Override
    public void completePurchase(@NonNull Purchase completedPurchase) {
        checkNotNull(completedPurchase, "completedTask cannot be null!");
        mShoppingRepository.completePurchase(completedPurchase,completedPurchase.getUser());
        mShoppingView.showPurchaseMarkedComplete();
        loadShopping(false, false);


    }



    @Override
    public void activatePurchase(@NonNull Purchase activePurchase ) {
        checkNotNull(activePurchase, "activeTask cannot be null!");


        mShoppingRepository.activatePurchase(activePurchase.getId(), activePurchase.getQuantity());
        mShoppingView.showPurchaseMarkedActive();
        loadShopping(false, false);
    }

    @Override
    public void clearCompletedShopping() {
        //mShoppingRepository.clearCompleted();
        // mTasksView.showCompletedTasksCleared();
        // loadTasks(false, false);
    }


    private void processTasks(List<Purchase> purchaseList) {
        if (purchaseList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            mShoppingView.showShopping(purchaseList);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
      //  mShoppingView.showAllFilterLabel();
    }

    private void processEmptyTasks() {

        mShoppingView.showNoShopping();

    }






}

