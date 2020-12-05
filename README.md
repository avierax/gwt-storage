# GWT Storage

![GWT3/J2CL compatible](https://img.shields.io/badge/GWT3/J2CL-compatible-brightgreen.svg)  [![License](https://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Chat on Gitter](https://badges.gitter.im/hal/elemento.svg)](https://gitter.im/gwtproject/gwt-modules) ![CI](https://github.com/gwtproject/gwt-storage/workflows/CI/badge.svg)

A future-proof port of the `com.google.gwt.storage.Storage` GWT module, with no dependency on `gwt-user` (besides the Java Runtime Emulation), to prepare for GWT 3 / J2Cl.

##  Migrating from `com.google.gwt.storage.Storage`

1. Add the dependency to your build.

   For Maven:

   ```xml
   <dependency>
       <groupId>org.gwtproject.storage</groupId>
       <artifactId>gwt-storage</artifactId>
       <version>HEAD-SNAPSHOT</version>
   </dependency>
   ```

   For Gradle:

   ```gradle
   implementation("org.gwtproject.storage:gwt-storage:HEAD-SNAPSHOT")
   ```

2. Update your GWT module to use

   ```xml
   <inherits name="org.gwtproject.storage.Storage" />
   ```

3. Change the `import`s in your Java source files:

   ```java
   import org.gwtproject.storage.client.*;
   ```

## Instructions

To build gwt-storage:

* run `mvn clean verify`

on the parent directory. This will build the artifact and run tests against the JVM, J2CL, and GWT2.

## System Requirements

**GWT Storage requires GWT 2.9.0 or newer!**


## Dependencies

GWT Storage depends on:

* gwt-core
* get-event


## Roadmap

 1. ~~Migrate the current gwt-2.8.2 code and remove incompatible dependencies~~ (**DONE**)
 2. ~~Remove dependency on JavaScriptObject~~ (**DONE**)
 3. ~~Remove all jsni constructions~~ (**DONE**)
 4. Add support for Native Events (pending issue from Gwt2)
 5. Optionally support receiving notifications of current session modifications
 6. Add support for automated testing multiple browser sessions from Gwt Unit Test

## Known issues

### Issues inherited from gwt2-storage

 - Native events are not yet correctly supported, they are emulated.
 - Testing multiple browser sessions in a single Gwt Unit Test is not supported by Gwt. Is it?

### Issues resulting from migration

 - It's unknown what mechanism should be used to substitute `GWT.reportUncaughtException`. See
  method `org.gwtproject.storage.client.StorageImpl.handleStorageEvent`


