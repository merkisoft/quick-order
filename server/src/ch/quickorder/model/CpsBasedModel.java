package ch.quickorder.model;

import ch.quickorder.util.ClusterPointConnection;
import com.clusterpoint.api.CPSConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

abstract public class CpsBasedModel {

    protected CPSConnection cpsConnection;

    protected DocumentBuilder documentBuilder;

    protected CpsBasedModel( String database) {
        try {
            cpsConnection = ClusterPointConnection.getInstance().getConnection( database);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
        }
    }
}
