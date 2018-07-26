package sky.victorlsn.test.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sky.victorlsn.test.util.AppTools;

/**
 * Created by victorlsn on 26/07/18.
 */

public class Movie implements Parcelable, Comparable<Movie> {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public static Comparator<Movie> TitleComparator = new Comparator<Movie>() {

        private final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

        @Override
        public int compare(Movie o1, Movie o2) {
            Matcher m1 = PATTERN.matcher(o1.getTitle());
            Matcher m2 = PATTERN.matcher(o2.getTitle());

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
    public static Comparator<Movie> DurationComparator = new Comparator<Movie>() {

        @Override
        public int compare(Movie o1, Movie o2) {
            int m1Duration = AppTools.convertTimeStringToMinutes(o1.duration);
            int m2Duration = AppTools.convertTimeStringToMinutes(o2.duration);

            return m2Duration - m1Duration;
        }
    };
    public static Comparator<Movie> ReleaseYearComparator = new Comparator<Movie>() {

        @Override
        public int compare(Movie o1, Movie o2) {
            return Integer.parseInt(o2.releaseYear) - Integer.parseInt(o1.releaseYear);
        }
    };

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("duration")
    private String duration;
    @SerializedName("release_year")
    private String releaseYear;
    @SerializedName("cover_url")
    private String coverUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.duration = in.readString();
        this.releaseYear = in.readString();
        this.coverUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.duration);
        dest.writeString(this.releaseYear);
        dest.writeString(this.coverUrl);
    }

    @Override
    public int compareTo(@NonNull Movie o) {
        return 0;
    }
}
