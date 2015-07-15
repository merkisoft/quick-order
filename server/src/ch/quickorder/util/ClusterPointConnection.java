package ch.quickorder.util;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.CPSRequest;
import com.clusterpoint.api.CPSResponse;

import javax.xml.transform.TransformerFactoryConfigurationError;
import java.util.HashMap;
import java.util.Map;

public class ClusterPointConnection {

    public ClusterPointConnection(String database) {
        cpsConnection = ClusterPointConnectionFactory.getInstance().getConnection(database);
    }

    private CPSConnection cpsConnection;

    synchronized public CPSResponse sendRequest(CPSRequest request) throws TransformerFactoryConfigurationError, Exception {
        return cpsConnection.sendRequest(request);
    }
}
