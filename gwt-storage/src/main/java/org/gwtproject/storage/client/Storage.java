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

import elemental2.dom.DomGlobal;
import elemental2.webstorage.WebStorageWindow;
import org.gwtproject.event.shared.HandlerRegistration;

/**
 * Implements the HTML5 Storage interface.
 *
 * <p>
 *
 * <p>You can obtain a Storage by either invoking {@link #getLocalStorageIfSupported()} or {@link
 * #getSessionStorageIfSupported()}.
 *
 * <p>
 *
 * <p><span style="color:red">Experimental API: This API is still under development and is subject
 * to change. </span>
 *
 * <p>
 *
 * <p>If Web Storage is NOT supported in the browser, these methods return <code>
 * null</code>.
 *
 * <p>
 *
 * <p>Note: Storage events into other windows are not supported.
 *
 * <p>
 *
 * <p>
 *
 * <p>This may not be supported on all browsers.
 *
 * @see <a href="http://www.w3.org/TR/webstorage/#storage-0">W3C Web Storage - Storage</a>
 * @see <a
 *     href="http://devworld.apple.com/safari/library/documentation/iPhone/Conceptual/SafariJSDatabaseGuide/Name-ValueStorage/Name-ValueStorage.html">Safari
 *     Client-Side Storage and Offline Applications Programming Guide - Key-Value Storage</a>
 * @see <a href="http://quirksmode.org/dom/html5.html#t00">Quirksmode.org - HTML5 Compatibility -
 *     Storage</a>
 * @see <a href="http://code.google.com/p/gwt-mobile-webkit/wiki/StorageApi">Wiki - Quickstart
 *     Guide</a>
 */
// TODO(pdr): Add support for Object values, instead of just Strings. The
// Storage API spec specifies this, but browser support poor at the moment.
// TODO(pdr): Add support for native events once browsers correctly implement
// storage events.
public final class Storage {

  static final StorageImpl impl = new StorageImplNonNativeEvents();
  private static Storage localStorage;
  private static Storage sessionStorage;
  // Contains either "localStorage" or "sessionStorage":
  private final String storage;

  /**
   * This class can never be instantiated externally. Use {@link #getLocalStorageIfSupported()} or
   * {@link #getSessionStorageIfSupported()} instead.
   */
  private Storage(String storage) {
    this.storage = storage;
  }

  /**
   * Registers an event handler for StorageEvents.
   *
   * @param handler
   * @return {@link HandlerRegistration} used to remove this handler
   * @see <a href="http://www.w3.org/TR/webstorage/#the-storage-event">W3C Web Storage - the storage
   *     event</a>
   */
  public static HandlerRegistration addStorageEventHandler(StorageEvent.Handler handler) {
    return impl.addStorageEventHandler(handler);
  }

  /**
   * Returns a Local Storage.
   *
   * <p>
   *
   * <p>The returned storage is associated with the <a
   * href="http://www.w3.org/TR/html5/browsers.html#origin">origin</a> of the Document.
   *
   * @return the localStorage instance, or <code>null</code> if Web Storage is NOT supported.
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-localstorage">W3C Web Storage -
   *     localStorage</a>
   */
  public static Storage getLocalStorageIfSupported() {
    if (localStorage == null && isLocalStorageSupported()) {
      localStorage = new Storage(StorageImpl.LOCAL_STORAGE);
    }
    return localStorage;
  }

  /**
   * Returns a Session Storage.
   *
   * <p>
   *
   * <p>The returned storage is associated with the current <a href=
   * "http://www.w3.org/TR/html5/browsers.html#top-level-browsing-context" >top-level browsing
   * context</a>.
   *
   * @return the sessionStorage instance, or <code>null</code> if Web Storage is NOT supported.
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-sessionstorage">W3C Web Storage -
   *     sessionStorage</a>
   */
  public static Storage getSessionStorageIfSupported() {
    if (sessionStorage == null && isSessionStorageSupported()) {
      sessionStorage = new Storage(StorageImpl.SESSION_STORAGE);
    }
    return sessionStorage;
  }

  /**
   * Returns <code>true</code> if the <code>localStorage</code> part of the Storage API is supported
   * on the running platform.
   */
  public static boolean isLocalStorageSupported() {
    return StorageSupportDetector.localStorageSupported;
  }

  /**
   * Returns <code>true</code> if the <code>sessionStorage</code> part of the Storage API is
   * supported on the running platform.
   */
  public static boolean isSessionStorageSupported() {
    return StorageSupportDetector.sessionStorageSupported;
  }

  /**
   * Returns <code>true</code> if the Storage API (both localStorage and sessionStorage) is
   * supported on the running platform.
   */
  public static boolean isSupported() {
    return isLocalStorageSupported() && isSessionStorageSupported();
  }

  /**
   * De-registers an event handler for StorageEvents.
   *
   * @param handler
   * @see <a href="http://www.w3.org/TR/webstorage/#the-storage-event">W3C Web Storage - the storage
   *     event</a>
   */
  public static void removeStorageEventHandler(StorageEvent.Handler handler) {
    impl.removeStorageEventHandler(handler);
  }

  /**
   * Removes all items in the Storage.
   *
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-clear">W3C Web Storage -
   *     Storage.clear()</a>
   */
  public void clear() {
    impl.clear(storage);
  }

  /**
   * Returns the item in the Storage associated with the specified key.
   *
   * @param key the key to a value in the Storage
   * @return the value associated with the given key
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-getitem">W3C Web Storage -
   *     Storage.getItem(k)</a>
   */
  public String getItem(String key) {
    return impl.getItem(storage, key);
  }

  /**
   * Returns the number of items in this Storage.
   *
   * @return number of items in this Storage
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-l">W3C Web Storage -
   *     Storage.length()</a>
   */
  public int getLength() {
    return impl.getLength(storage);
  }

  /**
   * Returns the key at the specified index.
   *
   * @param index the index of the key
   * @return the key at the specified index in this Storage
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-key">W3C Web Storage -
   *     Storage.key(n)</a>
   */
  public String key(int index) {
    return impl.key(storage, index);
  }

  /**
   * Removes the item in the Storage associated with the specified key.
   *
   * @param key the key to a value in the Storage
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-removeitem">W3C Web Storage -
   *     Storage.removeItem(k)</a>
   */
  public void removeItem(String key) {
    impl.removeItem(storage, key);
  }

  /**
   * Sets the value in the Storage associated with the specified key to the specified data.
   *
   * <p>Note: The empty string may not be used as a key.
   *
   * @param key the key to a value in the Storage
   * @param data the value associated with the key
   * @see <a href="http://www.w3.org/TR/webstorage/#dom-storage-setitem">W3C Web Storage -
   *     Storage.setItem(k,v)</a>
   */
  public void setItem(String key, String data) {
    // prevent the empty string due to a Firefox bug:
    // bugzilla.mozilla.org/show_bug.cgi?id=510849
    assert key.length() > 0;
    impl.setItem(storage, key, data);
  }

  // Still a separate class to prevent native calls on class load as it my break existing code.
  private static class StorageSupportDetector {
    static final boolean localStorageSupported = checkStorageSupport(StorageImpl.LOCAL_STORAGE);
    static final boolean sessionStorageSupported = checkStorageSupport(StorageImpl.SESSION_STORAGE);

    private static boolean checkStorageSupport(String storage) {
      String c = "_gwt_dummy_";
      WebStorageWindow window = WebStorageWindow.of(DomGlobal.window);
      final elemental2.webstorage.Storage storageObj =
          storage.equals(StorageImpl.LOCAL_STORAGE) ? window.localStorage : window.sessionStorage;
      try {
        storageObj.setItem(c, c);
        storageObj.removeItem(c);
        return true;
      } catch (Exception e) {
        return false;
      }
    }
  }
}
