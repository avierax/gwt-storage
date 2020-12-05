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

package org.gwtproject.storage.client;

import java.util.Map;

/** Tests {@link StorageMap}. */
public abstract class StorageMapTest extends MapInterfaceTest<String, String> {
  public Storage storage;

  public StorageMapTest() {
    super(false, false, true, true, true);
  }

  /**
   * Returns a {@link Storage} object.
   *
   * <p>Override to return either a LocalStorage or a SessionStorage
   *
   * @return a {@link Storage} object
   */
  abstract Storage getStorage();

  @Override
  public String getKeyNotInPopulatedMap() throws UnsupportedOperationException {
    return "nonExistingKey";
  }

  @Override
  public String getValueNotInPopulatedMap() throws UnsupportedOperationException {
    return "nonExistingValue";
  }

  @Override
  public Map<String, String> makeEmptyMap() throws UnsupportedOperationException {
    if (storage == null) {
      throw new UnsupportedOperationException(
          "StorageMap not supported because Storage is not supported.");
    }

    storage.clear();

    return new StorageMap(storage);
  }

  @Override
  public Map<String, String> makePopulatedMap() throws UnsupportedOperationException {
    if (storage == null) {
      throw new UnsupportedOperationException(
          "StorageMap not supported because Storage is not supported.");
    }

    storage.clear();

    storage.setItem("one", "January");
    storage.setItem("two", "February");
    storage.setItem("three", "March");
    storage.setItem("four", "April");
    storage.setItem("five", "May");

    return new StorageMap(storage);
  }
}
