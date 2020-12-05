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

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import org.gwtproject.event.shared.HandlerRegistration;

/** Tests {@link Storage}. */
public abstract class StorageTest extends GWTTestCase {
  protected Storage storage;
  protected StorageEvent.Handler handler;
  protected StorageEvent.Handler handler2;

  private native boolean isFirefox35OrLater() /*-{
    var geckoVersion = @com.google.gwt.dom.client.DOMImplMozilla::getGeckoVersion()();
    return (geckoVersion != -1) && (geckoVersion >= 1009001);
  }-*/;

  private native boolean isSafari3OrBefore() /*-{
    return @com.google.gwt.dom.client.DOMImplWebkit::isWebkit525OrBefore()();
  }-*/;

  @Override
  public String getModuleName() {
    return "org.gwtproject.storage.Storage";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    storage = getStorage();
    if (storage == null) {
      return; // do not run if not supported
    }

    // setup for tests by removing event handler
    if (handler != null) {
      Storage.removeStorageEventHandler(handler);
      handler = null;
    }
    if (handler2 != null) {
      Storage.removeStorageEventHandler(handler2);
      handler2 = null;
    }

    // setup for tests by emptying storage
    storage.clear();
  }

  @Override
  protected void gwtTearDown() {
    if (storage == null) {
      return; // do not run if not supported
    }

    // clean up by removing event handler
    if (handler != null) {
      Storage.removeStorageEventHandler(handler);
      handler = null;
    }
    if (handler2 != null) {
      Storage.removeStorageEventHandler(handler2);
      handler2 = null;
    }

    // clean up by emptying storage
    storage.clear();
  }

  /**
   * Returns a {@link Storage} object.
   *
   * <p>Override to return either a LocalStorage or a SessionStorage
   *
   * @return a {@link Storage} object
   */
  abstract Storage getStorage();

  public void testClear() {
    if (storage == null) {
      return; // do not run if not supported
    }

    storage.setItem("foo", "bar");
    assertEquals(1, storage.getLength());
    storage.clear();
    assertEquals(0, storage.getLength());
  }

  public void testGet() {
    if (storage == null) {
      return; // do not run if not supported
    }

    storage.setItem("foo1", "bar1");
    storage.setItem("foo2", "bar2");
    assertEquals("bar1", storage.getItem("foo1"));
    assertEquals("bar2", storage.getItem("foo2"));

    // getting a value of a key that hasn't been set should return null
    assertNull(
        "Getting a value of a key that hasn't been set should return null",
        storage.getItem("notset"));
  }

  public void testLength() {
    if (storage == null) {
      return; // do not run if not supported
    }

    storage.clear();
    assertEquals(0, storage.getLength());
    storage.setItem("abc", "def");
    assertEquals(1, storage.getLength());
    storage.setItem("ghi", "jkl");
    assertEquals(2, storage.getLength());
    storage.clear();
    assertEquals(0, storage.getLength());
  }

  public void testSet() {
    if (storage == null) {
      return; // do not run if not supported
    }

    assertNull(storage.getItem("foo"));
    assertEquals(0, storage.getLength());
    storage.setItem("foo", "bar1");
    assertEquals("bar1", storage.getItem("foo"));
    assertEquals(1, storage.getLength());
    storage.setItem("foo", "bar2");
    assertEquals("Should be able to overwrite an existing value", "bar2", storage.getItem("foo"));
    assertEquals(1, storage.getLength());

    // test that using the empty string as a key throws an exception in devmode
    if (!GWT.isScript()) {
      try {
        storage.setItem("", "baz");
        throw new Error("Empty string should be disallowed as a key.");
      } catch (AssertionError e) {
        // expected
      }
    }
  }

  public void testKey() {
    if (storage == null) {
      return; // do not run if not supported
    }

    // key(n) where n >= storage.length() should return null
    assertNull(storage.key(0));
    storage.setItem("a", "b");
    assertNull(storage.key(1));
    storage.clear();

    storage.setItem("foo1", "bar");
    assertEquals("foo1", storage.key(0));
    storage.setItem("foo2", "bar");
    // key(0) should be either foo1 or foo2
    assertTrue(storage.key(0).equals("foo1") || storage.key(0).equals("foo2"));
    // foo1 should be either key(0) or key(1)
    assertTrue(storage.key(0).equals("foo1") || storage.key(1).equals("foo1"));
    // foo2 should be either key(0) or key(1)
    assertTrue(storage.key(0).equals("foo2") || storage.key(1).equals("foo2"));
  }

  public void testRemoveItem() {
    if (storage == null) {
      return; // do not run if not supported
    }

    storage.setItem("foo1", "bar1");
    storage.setItem("foo2", "bar2");
    assertEquals("bar1", storage.getItem("foo1"));
    assertEquals("bar2", storage.getItem("foo2"));

    // removing a non-existent key should have no effect
    storage.removeItem("abc");
    assertEquals("bar1", storage.getItem("foo1"));
    assertEquals("bar2", storage.getItem("foo2"));

    // removing a key should remove that key and value
    storage.removeItem("foo1");
    assertNull(storage.getItem("foo1"));
    assertEquals("bar2", storage.getItem("foo2"));
    storage.removeItem("foo2");
    assertNull(storage.getItem("foo2"));
  }

  public void testClearStorageEvent() {
    if (storage == null) {
      return; // do not run if not supported
    }

    delayTestFinish(2000);
    storage.setItem("tcseFoo", "tcseBar");
    handler =
        event -> {
          assertNull(event.getKey());
          assertNull(event.getOldValue());
          assertNull(event.getNewValue());
          assertEquals(storage, event.getStorageArea());
          assertNotNull(event.getUrl());

          finishTest();
        };
    Storage.addStorageEventHandler(handler);
    storage.clear();
  }

  public void testSetItemStorageEvent() {
    if (storage == null) {
      return; // do not run if not supported
    }

    delayTestFinish(2000);
    storage.setItem("tsiseFoo", "tsiseBarOld");

    handler =
        event -> {
          assertEquals("tsiseFoo", event.getKey());
          assertEquals("tsiseBarNew", event.getNewValue());
          assertEquals("tsiseBarOld", event.getOldValue());
          assertEquals(storage, event.getStorageArea());
          assertNotNull(event.getUrl());

          finishTest();
        };
    Storage.addStorageEventHandler(handler);
    storage.setItem("tsiseFoo", "tsiseBarNew");
  }

  public void testRemoveItemStorageEvent() {
    if (storage == null) {
      return; // do not run if not supported
    }

    delayTestFinish(2000);
    storage.setItem("triseFoo", "triseBarOld");

    handler =
        event -> {
          assertEquals("triseFoo", event.getKey());
          finishTest();
        };
    Storage.addStorageEventHandler(handler);
    storage.removeItem("triseFoo");
  }

  public void testHandlerRegistration() {
    if (storage == null) {
      return; // do not run if not supported
    }

    final boolean[] eventFired = new boolean[1];

    delayTestFinish(3000);

    handler =
        event -> {
          fail("Storage change should not have fired.");
          eventFired[0] = true;
          finishTest();
        };
    HandlerRegistration registration = Storage.addStorageEventHandler(handler);
    registration.removeHandler();

    // these should fire events, but they should not be caught by handler
    storage.setItem("thrFoo", "thrBar");
    storage.clear();

    // schedule timer to make sure event didn't fire async
    new Timer() {
      @Override
      public void run() {
        if (!eventFired[0]) {
          finishTest();
        }
      }
    }.schedule(1000);
  }

  public void testEventInEvent() {
    if (storage == null) {
      return; // do not run if not supported
    }

    delayTestFinish(3000);
    storage.setItem("teieFoo", "teieBar");

    handler =
        event -> {
          if ("teieFoo".equals(event.getKey())) {
            storage.clear();
            storage.setItem("teieFoo2", "teieBar2");
            // firing events from within a handler should not corrupt the values.
            assertEquals("teieFoo", event.getKey());
            storage.setItem("teieFooEndTest", "thanks");
          }
          if ("teieFooEndTest".equals(event.getKey())) {
            finishTest();
          }
        };
    Storage.addStorageEventHandler(handler);
    storage.removeItem("teieFoo");
  }

  public void testMultipleEventHandlers() {
    if (storage == null) {
      return; // do not run if not supported
    }

    delayTestFinish(3000);

    final int[] eventHandledCount = new int[] {0};

    storage.setItem("tmehFoo", "tmehBar");

    handler =
        event -> {
          if ("tmehFoo".equals(event.getKey())) {
            eventHandledCount[0]++;
            if (eventHandledCount[0] == 2) {
              finishTest();
            }
          }
        };
    Storage.addStorageEventHandler(handler);

    handler2 =
        event -> {
          if ("tmehFoo".equals(event.getKey())) {
            eventHandledCount[0]++;
            if (eventHandledCount[0] == 2) {
              finishTest();
            }
          }
        };
    Storage.addStorageEventHandler(handler2);
    storage.removeItem("tmehFoo");
  }

  public void testEventStorageArea() {
    if (storage == null) {
      return; // do not run if not supported
    }

    delayTestFinish(2000);
    storage.setItem("tesaFoo", "tesaBar");
    handler =
        event -> {
          Storage eventStorage = event.getStorageArea();
          assertEquals(storage, eventStorage);
          boolean equalsLocal = Storage.getLocalStorageIfSupported().equals(eventStorage);
          boolean equalsSession = Storage.getSessionStorageIfSupported().equals(eventStorage);
          // assert that storage is either local or session, but not both.
          assertFalse(equalsLocal == equalsSession);

          finishTest();
        };
    Storage.addStorageEventHandler(handler);
    storage.clear();
  }

  public void testSupported() {
    // test the isxxxSupported() call
    if (isFirefox35OrLater()) {
      assertNotNull(storage);
      assertTrue(Storage.isLocalStorageSupported());
      assertTrue(Storage.isSessionStorageSupported());
      assertTrue(Storage.isSupported());
    }
    if (isSafari3OrBefore()) {
      assertNull(storage);
      assertFalse(Storage.isLocalStorageSupported());
      assertFalse(Storage.isSessionStorageSupported());
      assertFalse(Storage.isSupported());
    }
  }
}
