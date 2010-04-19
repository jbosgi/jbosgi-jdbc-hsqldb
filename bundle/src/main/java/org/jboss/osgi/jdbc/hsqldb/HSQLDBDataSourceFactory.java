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
package org.jboss.osgi.jdbc.hsqldb;

//$Id$

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.hsqldb.jdbcDriver;
import org.hsqldb.jdbc.jdbcDataSource;
import org.jboss.logging.Logger;
import org.jboss.osgi.spi.NotImplementedException;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * Th HSQLDB DataSourceFactory
 * 
 * @author thomas.diesler@jboss.com
 * @since 19-Apr-2010
 */
public final class HSQLDBDataSourceFactory implements DataSourceFactory
{
   // Provide logging
   private static final Logger log = Logger.getLogger(HSQLDBDataSourceFactory.class);
   
   @Override
   public Driver createDriver(Properties props) throws SQLException
   {
      if (props != null)
         log.warn("Driver properties, not implemented");
      
      Driver driver = new jdbcDriver();
      return driver;
   }
   
   @Override
   public DataSource createDataSource(Properties props) throws SQLException
   {
      jdbcDataSource dataSource = new jdbcDataSource();
      if (props != null)
      {
         String database = (String)props.get(JDBC_URL);
         String username = (String)props.get(JDBC_USER);
         String password = (String)props.get(JDBC_PASSWORD);
         dataSource.setDatabase(database);
         dataSource.setUser(username);
         dataSource.setPassword(password);
      }
      return dataSource;
   }

   @Override
   public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException
   {
      throw new NotImplementedException();
   }

   @Override
   public XADataSource createXADataSource(Properties props) throws SQLException
   {
      throw new NotImplementedException();
   }
}