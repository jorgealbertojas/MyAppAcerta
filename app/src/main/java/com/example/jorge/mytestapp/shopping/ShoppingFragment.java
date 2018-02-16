package com.example.jorge.mytestapp.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.addPurchase.AddPurchaseActivity;
import com.example.jorge.mytestapp.data.Purchase;
import com.example.jorge.mytestapp.data.source.remote.model.Product;
import com.example.jorge.mytestapp.purchaseDetail.PurchaseDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jorge on 15/02/2018.
 */

public class ShoppingFragment extends Fragment implements ShoppingContract.View {

    public static String EXTRA_PRODUCT_SHOPPING = "PRODUCT_SHOPPING";
    public static String EXTRA_BUNDLE_PRODUCT_SHOPPING = "BUNDLE_PRODUCT_SHOPPING";


    private ShoppingContract.Presenter mPresenter;

    private ShoppingAdapter mListAdapter;

    private View mNoShoppingView;
    private ImageView mNoShoppingIcon;
    private TextView mNoShoppingMainView;
    private TextView mNoShoppingAddView;
    private LinearLayout mShoppingView;

    private ImageView mProductImage;
    private TextView mCode;
    private TextView mProductName;




    private static Product mProduct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.shopping_fragment, container, false);

        // Set up Shopping view
        ListView listView = (ListView) root.findViewById(R.id.lv_shopping);
        listView.setAdapter(mListAdapter);

        mNoShoppingView = (LinearLayout) root.findViewById(R.id.ll_shopping);

        // Set up  no Shopping view
        mNoShoppingView = root.findViewById(R.id.noShopping);
        mNoShoppingView = (ImageView) root.findViewById(R.id.im_noShoppingIcon);
        mNoShoppingMainView = (TextView) root.findViewById(R.id.tv_noShoppingMain);
        mNoShoppingAddView = (TextView) root.findViewById(R.id.tv_noShoppingAdd);
        mNoShoppingAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPurchase();
            }
        });

        mProductImage = (ImageView) root.findViewById(R.id.im_product_image);
        mProductName = (TextView) root.findViewById(R.id.tv_product_name);
        mCode = (TextView) root.findViewById(R.id.tv_code);

        showProduct(mProduct);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_task);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewPurchase();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadShopping(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void showAddPurchase() {
        Intent intent = new Intent(getContext(), AddPurchaseActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRODUCT_SHOPPING, mProduct );
        intent.putExtra(EXTRA_BUNDLE_PRODUCT_SHOPPING, bundle);

        startActivityForResult(intent, AddPurchaseActivity.REQUEST_ADD_PURCHASE);
    }

    public static ShoppingFragment newInstance(Product product) {
        mProduct = product;
        return new ShoppingFragment();
    }

    @Override
    public void setPresenter(ShoppingContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showShopping(List<Purchase> listPurchase) {
        mListAdapter.replaceData(listPurchase);

        mShoppingView.setVisibility(View.VISIBLE);
        mNoShoppingView.setVisibility(View.GONE);
    }



    @Override
    public void showPurchaseDetailsUi(String productId, String user) {
        Intent intent = new Intent(getContext(), PurchaseDetailActivity.class);

        startActivity(intent);
    }

    @Override
    public void showPurchaseMarkedComplete() {
        showMessage(getString(R.string.purchase_marked_complete));
    }

    @Override
    public void showPurchaseMarkedActive() {
        showMessage(getString(R.string.purchase_marked_active));

    }

    @Override
    public void showCompletedShoppingCleared() {
        showMessage(getString(R.string.completed_purchase_cleared));
    }

    @Override
    public void showLoadingShoppingError() {
        showMessage(getString(R.string.loading_purchase_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void showNoShopping() {
        showNoShoppingViews(
                getResources().getString(R.string.no_shopping_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }


    @Override
    public void showNoActiveShopping() {

    }



    @Override
    public void showNoCompletedShopping() {
        showNoShoppingViews(
                getResources().getString(R.string.no_shopping_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showFilteringPopUpMenu() {

    }


    public void showProduct(Product product) {
        mProductName.setText(product.getName());
        mCode.setText(Integer.toString(product.getId()));

        Picasso.with(mProductImage.getContext())
                .load(product.getUrl_image_small())
                .fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(mProductImage);

    }

    private void showNoShoppingViews(String mainText, int iconRes, boolean showAddView) {
        mShoppingView.setVisibility(View.GONE);
        mNoShoppingView.setVisibility(View.VISIBLE);

        mNoShoppingMainView.setText(mainText);
        mNoShoppingIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoShoppingAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    private static class ShoppingAdapter extends BaseAdapter {

        private List<Purchase> mPurchaseList;
        private PurchaseItemListener mItemListener;

        public ShoppingAdapter(List<Purchase> tasks, PurchaseItemListener itemListener) {
            setList(tasks);
            mItemListener = itemListener;
        }

        public void replaceData(List<Purchase> purchaseList) {
            setList(purchaseList);
            notifyDataSetChanged();
        }

        private void setList(List<Purchase> tasks) {
            mPurchaseList = checkNotNull(tasks);
        }

        @Override
        public int getCount() {
            return mPurchaseList.size();
        }

        @Override
        public Purchase getItem(int i) {
            return mPurchaseList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.purchase_item, viewGroup, false);
            }

            final Purchase purchase = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.tv_name);
            titleTV.setText(purchase.getTitleForList());


            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onPurchaseClick(purchase);
                }
            });

            return rowView;
        }
    }

    public interface PurchaseItemListener {

        void onPurchaseClick(Purchase clickedTask);

        void onCompletePurchaseClick(Purchase completedTask);

        void onActivatePurchaseClick(Purchase activatedTask);
    }


}