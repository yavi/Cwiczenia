/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author yavi
 */
public class XMLFileDatabase implements Database{

    //////////////////////////////////////
    // XMLFileDatabase Definitions
    //////////////////////////////////////

    private Element root;
    private Document doc;

    public XMLFileDatabase(String file, String schema) throws java.net.MalformedURLException, IOException
    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schem;
        try {
            schem = sf.newSchema(new URL(schema));
        } catch (SAXException ex) {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        f.setNamespaceAware(true);
        f.setIgnoringElementContentWhitespace(true);
        f.setSchema(schem);

        Validator validator = schem.newValidator();
        validator.setErrorHandler(new ParseErrorHandler());
        try {
            validator.validate(new SAXSource(new InputSource()));
        } catch (SAXException ex) {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } 
    }


    // <editor-fold defaultstate="open" desc="Interface overrides">
    @Override
    public Element getRecordByNameKey(String table, String name)
    {

    }

    @Override
    public Element getRecordByIdKey(String table, int id)
    {

    }

    @Override
    public Element getRecordsByReferenceId(String refTable,int id)
    {

    }

    @Override
    public Element getRecords(String table)
    {

    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helper Classes">
    //////////////////////////
    //HELPER classes
    //////////////////////////

    private static class ParseErrorHandler implements ErrorHandler {
        public static String parseError;

        public ParseErrorHandler() {
            parseError = "";
        }

        @Override
        public void fatalError(SAXParseException ex) throws SAXException
        {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        @Override
        public void error(SAXParseException ex) throws SAXException
        {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        @Override
        public void warning(SAXParseException ex) throws SAXException
        {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.WARNING, null, ex);
        }
    }
    // </editor-fold>

}
