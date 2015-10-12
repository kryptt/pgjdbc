/*-------------------------------------------------------------------------
*
* Copyright (c) 2008-2014, PostgreSQL Global Development Group
*
*
*-------------------------------------------------------------------------
*/
package org.postgresql.test.jdbc4;

import java.sql.*;

import junit.framework.TestCase;

import org.postgresql.test.TestUtil;

public class EnumReportedTypeTest extends TestCase {

    private Connection _conn;

    public EnumReportedTypeTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        _conn = TestUtil.openDB();
        Statement stmt = _conn.createStatement();
        stmt.execute("CREATE TYPE cenum AS ENUM ('a', 'b', 'c', 'd')");
        stmt.execute("CREATE TEMP TABLE enumtest(id cenum, nm varchar)");
        stmt.execute("INSERT INTO enumtest VALUES('a', 'a'), ('b', 'b')");
        stmt.close();
    }

    protected void tearDown() throws SQLException {
        Statement stmt = _conn.createStatement();
        stmt.execute("DROP TABLE enumtest");
        stmt.execute("DROP TYPE cenum");
        stmt.close();
        TestUtil.closeDB(_conn);
    }

    public void testEnumMeta() throws SQLException {
      for (int i = 0; i < 1000; i++) {
        Statement stmt = _conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, nm FROM enumtest");
        ResultSetMetaData rsmd = rs.getMetaData();
        assertEquals("java.lang.Object", rsmd.getColumnClassName(1));
        assertEquals("java.lang.String", rsmd.getColumnClassName(2));
        rs.close();
        stmt.close();
      }
    }

}

