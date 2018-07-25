package mercari.victorlsn.mercari.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mercari.victorlsn.mercari.R;
import mercari.victorlsn.mercari.beans.Product;
import mercari.victorlsn.mercari.enums.SortEnum;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private List<Product> sortedProducts;
    private LayoutInflater mInflater;
    private Context context;

    private onItemClickListener mItemClickListener;

    public ProductAdapter(Context context, List<Product> products) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.products = products;
    }

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void sortBy(SortEnum sortingMethod) {
        sortedProducts = new ArrayList<>(products);
        if (sortingMethod != null) {
            switch (sortingMethod) {
                case NAME:
                    Collections.sort(sortedProducts, Product.NameComparator);
                    break;

                case STATUS:
                    Collections.sort(sortedProducts, Product.StatusComparator);
                    break;

                case PRICE:
                    Collections.sort(sortedProducts, Product.PriceComparator);
                    break;

                case LIKES:
                    Collections.sort(sortedProducts, Product.LikesComparator);
                    break;

                case COMMENTS:
                    Collections.sort(sortedProducts, Product.CommentsComparator);
                    break;

                default:
                    break;
            }

            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = sortedProducts.get(position);

        Glide.with(context).load(product.getPhoto()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.image_not_found)).into(holder.productImage);

        if (product.getStatus().equals("on_sale")) holder.soldOutImage.setVisibility(View.GONE);
        else holder.soldOutImage.setVisibility(View.VISIBLE);

        holder.productName.setText(product.getName());
        holder.productLikes.setText(String.valueOf(product.getLikes()));
        holder.productComments.setText(String.valueOf(product.getComments()));
        holder.productPrice.setText(String.format("$ %s", product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return sortedProducts.size();
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, String url);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.product_iv)
        ImageView productImage;
        @BindView(R.id.sold_out_iv)
        ImageView soldOutImage;
        @BindView(R.id.name_tv)
        TextView productName;
        @BindView(R.id.likes_tv)
        TextView productLikes;
        @BindView(R.id.comments_tv)
        TextView productComments;
        @BindView(R.id.price_tv)
        TextView productPrice;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(productImage, sortedProducts.get(getAdapterPosition()).getPhoto());
            }
        }
    }
}