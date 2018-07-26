package sky.victorlsn.test.beans;

import java.io.Serializable;

/**
 * Created by victorlsn on 26/07/18.
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
