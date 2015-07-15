package ch.quickorder.util;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.CPSRequest;
import com.clusterpoint.api.CPSResponse;

import javax.xml.transform.TransformerFactoryConfigurationError;
import java.util.HashMap;
import java.util.Map;

public class ClusterPointConnectionFactory {

    private static ClusterPointConnectionFactory instance;

    public static ClusterPointConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ClusterPointConnectionFactory();
        }

        return instance;
    }

    private static Map< String, CPSConnection> dbConnections = new HashMap<>();

    public ClusterPointConnectionFactory() {
    }

    public CPSConnection getConnection( String database) {
        CPSConnection cpsConnection = dbConnections.get( database);

        if (cpsConnection == null) {
            try {
                cpsConnection = new CPSConnection("tcp://cloud-eu-1.clusterpoint.com:9007", database,
                        "testuser", "Test42", "1302", "document", "//document/id");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return cpsConnection;
    }
}
