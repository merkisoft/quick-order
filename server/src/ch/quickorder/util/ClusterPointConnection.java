package ch.quickorder.util;

import com.clusterpoint.api.CPSConnection;

import java.util.HashMap;
import java.util.Map;

public class ClusterPointConnection {

    private static ClusterPointConnection instance;

    public static ClusterPointConnection getInstance() {
        if (instance == null) {
            instance = new ClusterPointConnection();
        }

        return instance;
    }

    private Map< String, CPSConnection> dbConnections = new HashMap<>();

    public ClusterPointConnection() {
    }

    public CPSConnection getConnection( String database) {
        CPSConnection cpsConnection = dbConnections.get( database);

        if (cpsConnection == null) {
            try {
                cpsConnection = new CPSConnection("tcps://cloud-eu-0.clusterpoint.com:9008", database,
                        "testuser", "Test42", "1302", "document", "//document/id");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return cpsConnection;
    }
}
