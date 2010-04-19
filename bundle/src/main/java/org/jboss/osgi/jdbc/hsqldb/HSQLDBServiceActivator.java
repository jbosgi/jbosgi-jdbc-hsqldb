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
import java.util.Dictionary;
import java.util.Hashtable;

import org.hsqldb.jdbcDriver;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * A BundleActivator for the MBeanServer related services
 * 
 * @author thomas.diesler@jboss.com
 * @since 19-Apr-2010
 */
public class HSQLDBServiceActivator implements BundleActivator
{
   @Override
   public void start(BundleContext context)
   {
      DataSourceFactory dataSourceFactory = new HSQLDBDataSourceFactory();
      
      Driver driver = new jdbcDriver();
      Dictionary<String, String> props = new Hashtable<String, String>();
      props.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS, driver.getClass().getName());
      props.put(DataSourceFactory.OSGI_JDBC_DRIVER_NAME, driver.getClass().getPackage().getSpecificationTitle());
      props.put(DataSourceFactory.OSGI_JDBC_DRIVER_VERSION, driver.getMajorVersion() + "." + driver.getMinorVersion());
      context.registerService(DataSourceFactory.class.getName(), dataSourceFactory, props);
   }

   @Override
   public void stop(BundleContext context) throws Exception
   {
   }
}