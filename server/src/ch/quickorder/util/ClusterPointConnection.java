package ch.quickorder.util;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.CPSRequest;
import com.clusterpoint.api.CPSResponse;

import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.IOException;

public class ClusterPointConnection {

    private ThreadLocal<CPSConnection> cpsConnection = new ThreadLocal<>();

    private String database;

    public ClusterPointConnection(String database) {
        this.database = database;
    }

    synchronized public CPSResponse sendRequest(CPSRequest request) throws TransformerFactoryConfigurationError, Exception {
        return sendRequest0(request, 0);
    }
    private CPSResponse sendRequest0(CPSRequest request, int attempt) throws TransformerFactoryConfigurationError, Exception {
        CPSConnection cpsConnection = this.cpsConnection.get();
        if (cpsConnection == null) {
            cpsConnection = ClusterPointConnectionFactory.getInstance().getConnection(database);
            this.cpsConnection.set(cpsConnection);
        }

        try {
            return cpsConnection.sendRequest(request);
        } catch (IOException e) {
            System.out.println(LogPrefix.currentTime(database + " closing db"));
            cpsConnection.close();
            this.cpsConnection.set(null);

            if (attempt > 5) throw e;
            System.out.println(LogPrefix.currentTime(database + " automatic retry"));
            return sendRequest0(request, attempt + 1);  // retry
        }
    }
}
