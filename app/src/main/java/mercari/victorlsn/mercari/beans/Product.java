package mercari.victorlsn.mercari.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by victorlsn on 23/07/18.
 */

public class Product implements Parcelable, Comparable<Product> {
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    public static Comparator<Product> NameComparator = new Comparator<Product>() {

        private final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

        @Override
        public int compare(Product o1, Product o2) {
            Matcher m1 = PATTERN.matcher(o1.getName());
            Matcher m2 = PATTERN.matcher(o2.getName());

            while (m1.find() && m2.find()) {
                // matcher.group(1) fetches any non-digits captured by the
                // first parentheses in PATTERN.
                int nonDigitCompare = m1.group(1).compareTo(m2.group(1));
                if (0 != nonDigitCompare) {
                    return nonDigitCompare;
                }

                // matcher.group(2) fetches any digits captured by the
                // second parentheses in PATTERN.
                if (m1.group(2).isEmpty()) {
                    return m2.group(2).isEmpty() ? 0 : -1;
                } else if (m2.group(2).isEmpty()) {
                    return +1;
                }

                BigInteger n1 = new BigInteger(m1.group(2));
                BigInteger n2 = new BigInteger(m2.group(2));
                int numberCompare = n1.compareTo(n2);
                if (0 != numberCompare) {
                    return numberCompare;
                }
            }

            // Handle if one string is a prefix of the other.
            // Nothing comes before something.
            return m1.hitEnd() && m2.hitEnd() ? 0 :
                    m1.hitEnd() ? -1 : +1;

        }
    };
    public static Comparator<Product> StatusComparator = new Comparator<Product>() {

        @Override
        public int compare(Product o1, Product o2) {
            return o1.status.compareTo(o2.status);
        }
    };
    public static Comparator<Product> PriceComparator = new Comparator<Product>() {

        @Override
        public int compare(Product o1, Product o2) {
            return o2.price - o1.price;
        }
    };
    public static Comparator<Product> LikesComparator = new Comparator<Product>() {

        @Override
        public int compare(Product o1, Product o2) {
            return o2.likes - o1.likes;
        }
    };
    public static Comparator<Product> CommentsComparator = new Comparator<Product>() {

        @Override
        public int compare(Product o1, Product o2) {
            return o2.comments - o1.comments;
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private String status;
    @SerializedName("num_likes")
    private int likes;
    @SerializedName("num_comments")
    private int comments;
    @SerializedName("price")
    private int price;
    @SerializedName("photo")
    private String photo;

    private Product(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.photo = in.readString();
        this.likes = in.readInt();
        this.comments = in.readInt();
        this.price = in.readInt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeString(this.photo);
        dest.writeInt(this.likes);
        dest.writeInt(this.comments);
        dest.writeInt(this.price);
    }

    @Override
    public int compareTo(@NonNull Product o) {
        return 0;
    }
}
