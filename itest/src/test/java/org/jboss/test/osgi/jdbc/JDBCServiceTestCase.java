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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.jboss.osgi.testing.OSGiFrameworkTest;
import org.jboss.osgi.testing.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.osgi.jdbc.bundle.JDBCTestActivator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * An abstract JMX test case.
 * 
 * @author thomas.diesler@jboss.com
 * @since 19-Apr-2010
 */
public class JDBCServiceTestCase extends OSGiFrameworkTest
{
   @BeforeClass
   public static void beforeHSQLDBTestCase() throws Exception
   {
      // Install/Start the jboss-osgi-hsqldb bundle
      String bundleName = "jboss-osgi-hsqldb-" + System.getProperty("project.version");
      URL bundleURL = new File("../bundle/target/" + bundleName + ".jar").toURI().toURL();
      Bundle bundle = systemContext.installBundle(bundleURL.toExternalForm());
      bundle.start();
   }
   
   @Test
   public void testInMemoryDatabase() throws Exception
   {
      // Load the HSQLDB driver
      Class.forName("org.hsqldb.jdbcDriver");
      
      // Create some test data 
      Connection con = DriverManager.getConnection(JDBCTestActivator.JDBC_TEST_URL, "sa", "");
      try
      {
         Statement stm = con.createStatement();
         stm.execute("CREATE TABLE customer (name varchar)");
         stm.execute("INSERT INTO customer VALUES ('Acme Corp')");
         stm.execute("SHUTDOWN COMPACT");
      }
      finally
      {
         con.close();
      }
      
      // Build a test bundle with shrinkwrap
      final JavaArchive archive = Archives.create("hsqldb-test", JavaArchive.class);
      archive.addClass(JDBCTestActivator.class);
      archive.setManifest(new Asset()
      {
         public InputStream openStream()
         {
            OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
            builder.addBundleSymbolicName(archive.getName());
            builder.addBundleManifestVersion(2);
            builder.addBundleActivator(JDBCTestActivator.class.getName());
            builder.addImportPackages("org.osgi.framework", "org.osgi.service.jdbc", "javax.sql");
            return builder.openStream();
         }
      });
      
      Bundle bundle = installBundle(archive);
      try
      {
         bundle.start();
         assertBundleState(Bundle.ACTIVE, bundle.getState());
         
      }
      finally
      {
         bundle.uninstall();
      }
   }
}