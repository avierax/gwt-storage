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

import static elemental2.dom.DomGlobal.window;

import elemental2.webstorage.WebStorageWindow;
import jsinterop.base.JsPropertyMap;

/**
 * Implementation of Storage with non-native events.
 *
 * <p>Implementation of StorageEvents is incomplete for many browsers. This class amends the
 * properties consistently with W3C's StorageEvent.
 */
class StorageImplNonNativeEvents extends StorageImpl {

  private static StorageEvent createStorageEvent(
      String key, String oldValue, String newValue, String storageName) {
    JsPropertyMap<Object> init = JsPropertyMap.of();
    init.set("key", key);
    init.set("oldValue", oldValue);
    init.set("newValue", newValue);
    init.set("url", window.location.href);
    init.set(
        "storageArea",
        "localStorage".equals(storageName)
            ? WebStorageWindow.of(window).localStorage
            : WebStorageWindow.of(window).sessionStorage);
    return StorageEvent.createEvent(init);
  }

  private static void fireStorageEvent(
      String key, String oldValue, String newValue, String storage) {
    if (hasStorageEventHandlers()) {
      StorageEvent se = createStorageEvent(key, oldValue, newValue, storage);
      handleStorageEvent(se);
    }
  }

  @Override
  public void clear(String storage) {
    super.clear(storage);
    fireStorageEvent(null, null, null, storage);
  }

  @Override
  public void removeItem(String storage, String key) {
    String oldValue = getItem(storage, key);
    super.removeItem(storage, key);
    fireStorageEvent(key, oldValue, null, storage);
  }

  @Override
  public void setItem(String storage, String key, String data) {
    String oldValue = getItem(storage, key);
    super.setItem(storage, key, data);
    fireStorageEvent(key, oldValue, data, storage);
  }

  @Override
  protected void addStorageEventHandler0() {
    // no-op
  }

  @Override
  protected void removeStorageEventHandler0() {
    // no-op
  }
}
