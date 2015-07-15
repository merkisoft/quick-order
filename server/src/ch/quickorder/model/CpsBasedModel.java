package ch.quickorder.model;

import ch.quickorder.util.ClusterPointConnection;
import ch.quickorder.util.ClusterPointConnectionFactory;
import com.clusterpoint.api.CPSConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract public class CpsBasedModel {

    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat( "HH:mm:ss.SSS ");
        }
    };

    protected String currentTime() {
        return dateFormat.get().format(new Date( System.currentTimeMillis())) + getClass().getSimpleName() + " ";
    }

    protected ClusterPointConnection cpsConnection;

    protected DocumentBuilder documentBuilder;

    protected CpsBasedModel( String database) {
        try {
            cpsConnection = new ClusterPointConnection( database);
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
