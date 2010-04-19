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
package org.jboss.test.osgi.jdbc.bundle;

//$Id$

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * A BundleActivator for the HSQLDB related services
 * 
 * @author thomas.diesler@jboss.com
 * @since 19-Apr-2010
 */
public class JDBCTestActivator implements BundleActivator
{
   public static final String JDBC_TEST_URL = "jdbc:hsqldb:target/hsqldb/testdb";

   @Override
   public void start(BundleContext context) throws BundleException
   {
      ServiceReference sref = context.getServiceReference(DataSourceFactory.class.getName());
      DataSourceFactory factory = (DataSourceFactory)context.getService(sref);

      try
      {
         Properties props = new Properties();
         props.put(DataSourceFactory.JDBC_URL, JDBC_TEST_URL);
         props.put(DataSourceFactory.JDBC_USER, "sa");
         props.put(DataSourceFactory.JDBC_PASSWORD, "");

         DataSource dataSource = factory.createDataSource(props);
         Connection con = dataSource.getConnection();
         try
         {
            Statement stm = con.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT * FROM customer");
            resultSet.next();
            
            String value = resultSet.getString("name");
            System.out.println(value);
         }
         finally
         {
            con.close();
         }
      }
      catch (SQLException ex)
      {
         throw new BundleException("Database access error", ex);
      }
   }

   @Override
   public void stop(BundleContext context) throws Exception
   {
   }
}