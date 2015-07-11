package ch.quickorder.model;

import ch.quickorder.util.ClusterPointConnection;
import com.clusterpoint.api.CPSConnection;

abstract public class CpsBasedModel {

    protected CPSConnection cpsConnection;

    protected CpsBasedModel( String database) {
        try {
            cpsConnection = ClusterPointConnection.getInstance().getConnection( database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
