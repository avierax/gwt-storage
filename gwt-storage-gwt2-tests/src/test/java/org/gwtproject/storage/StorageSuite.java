/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.gwtproject.storage;

import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import org.gwtproject.storage.client.LocalStorageMapTest;
import org.gwtproject.storage.client.LocalStorageTest;
import org.gwtproject.storage.client.SessionStorageMapTest;
import org.gwtproject.storage.client.SessionStorageTest;

/** Suite for all Storage tests. */
public class StorageSuite {
  public static Test suite() {
    GWTTestSuite suite = new GWTTestSuite("Storage Tests");

    suite.addTestSuite(LocalStorageTest.class);
    suite.addTestSuite(SessionStorageTest.class);
    suite.addTestSuite(LocalStorageMapTest.class);
    suite.addTestSuite(SessionStorageMapTest.class);

    return suite;
  }
}
