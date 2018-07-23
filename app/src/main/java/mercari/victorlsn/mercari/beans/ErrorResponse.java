package mercari.victorlsn.mercari.beans;

import java.io.Serializable;

/**
 * Created by victorlsn on 23/07/18.
 */

public class ErrorResponse implements Serializable {
    private APIError error;

    public APIError getError() {
        return error;
    }

    public void setError(APIError error) {
        this.error = error;
    }
}
