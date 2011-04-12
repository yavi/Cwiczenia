/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author yavi
 */
public class DatabaseFactory {
    public Database getXMLDatabase(String file, String schema) {
        return new XMLFileDatabase(file,schema);
    }
}
