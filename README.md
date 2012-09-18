Interface for a local [LandSFACTS](http://www.macaulay.ac.uk/LandSFACTS/) instance.

# Getting started

The dependency for the interface can then be added.

```xml
<dependencies>
  <!-- Other dependencies may be here too -->
  <dependency>
    <groupId>org.uncertweb</groupId>
    <artifactId>landsfacts-interface</artifactId>
    <version>0.1-SNAPSHOT</version>
  </dependency>
</dependencies>
```


## Build

If you wish to build the project from source, rename `landsfacts.sample.properties` in `src/test/resources` to `landsfacts.properties`. In this file, change the `executable.path` property to the location of the LandSFACTS executable on your local machine. To build:

```console
$ mvn clean package
```