package sky.victorlsn.test.ui.adapters;

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
import sky.victorlsn.test.R;
import sky.victorlsn.test.beans.Movie;
import sky.victorlsn.test.enums.SortEnum;

/**
 * Created by victorlsn on 26/07/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies;
    private List<Movie> sortedMovies;
    private LayoutInflater mInflater;
    private Context context;

    private onItemClickListener mItemClickListener;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.movies = movies;
        this.sortedMovies = movies;
    }

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void sortBy(SortEnum sortingMethod) {
        sortedMovies = new ArrayList<>(movies);
        if (sortingMethod != null) {
            switch (sortingMethod) {
                case TITLE:
                    Collections.sort(sortedMovies, Movie.TitleComparator);
                    break;

                case DURATION:
                    Collections.sort(sortedMovies, Movie.DurationComparator);
                    break;

                case RELEASE_YEAR:
                    Collections.sort(sortedMovies, Movie.ReleaseYearComparator);
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
        Movie movie = sortedMovies.get(position);

        Glide.with(context).load(movie.getCoverUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.image_not_found)).into(holder.movieImage);
        holder.titleText.setText(movie.getTitle());
//        holder.productLikes.setText(String.valueOf(product.getLikes()));
//        holder.productComments.setText(String.valueOf(product.getComments()));
//        holder.productPrice.setText(String.format("$ %s", product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return sortedMovies.size();
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, Movie movie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.cover_iv)
        ImageView movieImage;
        @BindView(R.id.title_tv)
        TextView titleText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(movieImage, sortedMovies.get(getAdapterPosition()));
            }
        }
    }
}