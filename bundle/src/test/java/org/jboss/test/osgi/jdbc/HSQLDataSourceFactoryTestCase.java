/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.osgi.jdbc;

//$Id$

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.jboss.osgi.jdbc.hsqldb.HSQLDBDataSourceFactory;
import org.junit.Test;
import org.osgi.service.jdbc.DataSourceFactory;


/**
 * An abstract JMX test case.
 * 
 * @author thomas.diesler@jboss.com
 * @since 19-Apr-2010
 */
public class HSQLDataSourceFactoryTestCase 
{
   @Test
   public void testInMemoryDatabase() throws SQLException
   {
      Properties props = new Properties();
      props.put(DataSourceFactory.JDBC_URL, "jdbc:hsqldb:mem:aname");
      props.put(DataSourceFactory.JDBC_USER, "sa");
      props.put(DataSourceFactory.JDBC_PASSWORD, "");
      
      DataSourceFactory dsFactory = new HSQLDBDataSourceFactory();
      DataSource dataSource = dsFactory.createDataSource(props);
      assertNotNull("DataSource not null", dataSource);
      
      Connection con = dataSource.getConnection();
      assertNotNull("Connection not null", con);
      
      Statement stm = con.createStatement();
      stm.execute("CREATE TABLE customer (name varchar)");
      stm.execute("INSERT INTO customer VALUES ('Acme Corp')");
      ResultSet resultSet = stm.executeQuery("SELECT * FROM customer");
      assertTrue("ResultSet next", resultSet.next());
      String value = resultSet.getString("name");
      assertEquals("Acme Corp", value);
   }
}