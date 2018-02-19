package com.example.jorge.mytestapp.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.mytestapp.R;
import com.example.jorge.mytestapp.data.source.onLine.ProductServiceImpl;
import com.example.jorge.mytestapp.data.source.onLine.model.Product;
import com.example.jorge.mytestapp.shopping.ShoppingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 14/02/2018.
 */

public class ProductFragment extends Fragment implements ProductContract.View {

    public static String EXTRA_PRODUCT = "PRODUCT";
    public static String EXTRA_BUNDLE_PRODUCT = "BUNDLE_PRODUCT";

    private ProductContract.UserActionsListener mActionsListener;

    private ProductsAdapter mListAdapter;
    private RecyclerView mRecyclerView;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "RECYCLER_VIEW_STATE";
    private Parcelable mListState;

    public ProductFragment() {
    }

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ProductsAdapter(new ArrayList<Product>(0), mItemListener);
        mActionsListener = new ProductPresenter(new ProductServiceImpl(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadingProduct();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View root = inflater.inflate(R.layout.products_fragment, container, false);

        ImageView shopping  = (ImageView) root.findViewById(R.id.iv_shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAllShopping();
            }
        });

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mActionsListener.loadingProduct();
            }
        });


        if (savedInstanceState== null){
            initRecyclerView(root);
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }else{
            initRecyclerView(root);
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }


        return root;
    }




    ItemListener mItemListener = new ItemListener() {
        @Override
        public void onProductClick(Product product) {

            mActionsListener.openDetail(product);
        }
    };

    @Override
    public void setLoading(final boolean isActive) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showProduct(List<Product> productList) {
        mListAdapter.replaceData(productList);
    }

    @Override
    public void showAllShopping() {

        Intent intent = new Intent(getActivity(), ShoppingActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRODUCT, null);

        intent.putExtra(EXTRA_BUNDLE_PRODUCT, bundle);
        startActivity(intent);
    }

    private void initRecyclerView(View root){
        mRecyclerView= (RecyclerView) root.findViewById(R.id.rv_products_list);
        mRecyclerView.setAdapter(mListAdapter);

        int numColumns = 1;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
    }

    private static class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

        private List<Product> mProducts;
        private ItemListener mItemListener;

        public ProductsAdapter(List<Product> products, ItemListener itemListener) {
            setList(products);
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.product_item, parent, false);

            return new ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Product product = mProducts.get(position);

            Picasso.with(viewHolder.productImage.getContext())
                    .load(product.getUrl_image_small())
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.productImage);

            viewHolder.productName.setText(product.getName());
        }

        public void replaceData(List<Product> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Product> notes) {
            mProducts = notes;
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }

        public Product getItem(int position) {
            return mProducts.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView productImage;
            public TextView productName;
            private ItemListener mItemListener;

            public ViewHolder(View itemView, ItemListener listener) {
                super(itemView);
                mItemListener = listener;
                productName= (TextView) itemView.findViewById(R.id.tv_product_name);
                productImage = (ImageView) itemView.findViewById(R.id.im_product_image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Product product = getItem(position);
                mItemListener.onProductClick(product);

                Intent intent = new Intent(v.getContext(), ShoppingActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_PRODUCT, product );

                intent.putExtra(EXTRA_BUNDLE_PRODUCT, bundle);
                v.getContext().startActivity(intent);



            }
        }
    }

    public interface ItemListener {

        void onProductClick(Product clickedNote);
    }
}

