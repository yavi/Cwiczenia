/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.*;
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

        f.setNamespaceAware(false);
        f.setIgnoringElementContentWhitespace(true);
        f.setSchema(schem);

        Validator validator = schem.newValidator();
        validator.setErrorHandler(new ParseErrorHandler());
        try {
            validator.validate(new SAXSource(new InputSource(file)));
        } catch (SAXException ex) {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        DocumentBuilder builder;
        try {
            builder = f.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        root = doc.getDocumentElement();
    }


    // <editor-fold defaultstate="open" desc="Interface overrides - get*">
    @Override
    public Element getRecordByNameKey(String table, String fieldName, String value)
    {
        Element tb = getTable(table);
        return findByFieldValue(tb,fieldName,value);
    }

    @Override
    public Element getRecordSetByRegexp(String table, String fieldName, String value)
    {
        Element tb = getTable(table);
        return findByFieldValueEx(tb,fieldName,value);
    }

    @Override
    public Element getRecordByIdKey(String table, int id)
    {
        Element tb = getTable(table);
        return findByTagAndAttr(tb,"db:record","id","1");
    }

    @Override
    public Element getRecordsByReferenceId(String refTable,int id)
    {
        //TODO: Not implemented
        return null;
    }

    @Override
    public Element getRecords(String table)
    {
        Element resultSet = doc.createElement("resultSet");
        NodeList nl = getTable(table).getElementsByTagName("db:record");

        for(int i=0;i<nl.getLength();++i)
        {
            Element cp = (Element)nl.item(i).cloneNode(true);
            resultSet.appendChild(cp);
        }

        return resultSet;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="private helper methods">

    private Element findByTagAndAttr(Element e, String tagName, String attrName, String value)
    {
        NodeList tb = e.getElementsByTagName(tagName);
        if(tb == null){
            Logger.getLogger(XMLFileDatabase.class.getName()).log(Level.WARNING, value + " NOT FOUND!");
            return null;
        }

        for(int i=0;i<tb.getLength();++i)
        {
            NamedNodeMap attr = tb.item(i).getAttributes();
            Node n = attr.getNamedItem(attrName);
            String tbn = n.getTextContent();
            if(tbn.equals(value))
                return (Element)tb.item(i);
        }

        return null;
    }

    private Element findByFieldValue(Element table, String fieldName, String value)
    {
        NodeList tb = table.getElementsByTagName("db:record");

        for(int i=0;i<tb.getLength();++i)
        {
            Element n = (Element) tb.item(i);
            Element f = findByTagAndAttr(n,"db:stringField","name",fieldName);
                            
            if(f == null)
                f = findByTagAndAttr(n,"db:intField","name",fieldName);

            if(f != null)
            {
                if(f.getTextContent().equals(value))
                    return n;
            }
        }
        return null;
    }

    private Element findByFieldValueEx(Element table, String fieldName, String value)
    {
        Element x = doc.createElement("resultSet");
        Pattern pt = Pattern.compile(value);
        NodeList tb = table.getElementsByTagName("db:record");

        for(int i=0;i<tb.getLength();++i)
        {
            Element n = (Element) tb.item(i);
            Element f = findByTagAndAttr(n,"db:stringField","name",fieldName);
            if(f == null)
                f = findByTagAndAttr(n,"db:intField","name",fieldName);

            if(f != null)
            {
                    if(pt.matcher(f.getTextContent()).matches())
                    {
                        Element cp = (Element) n.cloneNode(true);
                        x.appendChild(cp);
                    }
            }
        }
        return x;
    }


    private Element getTable(String table)
    {
        return findByTagAndAttr(root,"db:dataTable","name",table);
    }
    // </editor-fold>

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
