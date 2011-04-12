/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author yavi
 */
public class DatabaseFactory {
    public Database getXMLDatabase(String file, String schema) throws MalformedURLException, IOException {
        return new XMLFileDatabase(file,schema);
    }
}
