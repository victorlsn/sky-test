package mercari.victorlsn.mercari.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mercari.victorlsn.mercari.R;
import mercari.victorlsn.mercari.beans.Product;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private LayoutInflater mInflater;
    private Context context;

    private onItemClickListener mItemClickListener;

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, String url);
    }

    public ProductAdapter(Context context, List<Product> products) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);

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
        return products.size();
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
                mItemClickListener.onItemClickListener(productImage, products.get(getAdapterPosition()).getPhoto());
            }
        }
    }

    String getItem(int id) {
        return products.get(id).getPhoto();
    }
}