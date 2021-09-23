package org.apache.jackrabbit.vault.fs.io;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.jackrabbit.vault.fs.impl.io.DocViewSAXHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DocViewParser {

    private SAXParser createSaxParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        SAXParser parser = factory.newSAXParser();
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return parser;
    }

    /**
     * 
     * @param inputSource
     * @param handler
     * @throws IOException 
     */
    public void parse(String rootNodePath, InputSource inputSource, DocViewParserHandler handler) throws IOException, RepositoryException {
        final SAXParser parser;
        try {
            parser = createSaxParser();
        } catch (ParserConfigurationException|SAXException e) {
            throw new IllegalStateException("Could not create SAX parser" + e.getMessage(), e);
        }
        try {
            parser.parse(inputSource, new DocViewSAXHandler(handler, rootNodePath));
        } catch (SAXException e) {
            // all other exception just pass on as IOException
            throw new IllegalArgumentException("Could not parse " + inputSource, e);
        }
    }
}
