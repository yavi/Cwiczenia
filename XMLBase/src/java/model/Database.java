/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import org.w3c.dom.Element;

/**
 *
 * @author yavi
 */
public interface Database {
    Element getRecordByNameKey(String table, String name);
    Element getRecordSetByRegexp(String table, String fieldName, String value);
    Element getRecordByIdKey(String table, int id);
    Element getRecordsByReferenceId(String refTable,int id);
    Element getRecords(String table);
}
