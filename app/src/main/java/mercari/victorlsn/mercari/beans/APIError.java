package mercari.victorlsn.mercari.beans;

import java.io.Serializable;

/**
 * Created by victorlsn on 23/07/18.
 */

public class APIError implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
