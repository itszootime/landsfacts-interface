Interface for a local [LandSFACTS](http://www.macaulay.ac.uk/LandSFACTS/) instance.

## Getting started

The interface is available on the UncertWeb Maven repository, hosted at the University of Münster. Adding the following snippet to your pom.xml file will include the repository in your project.

```xml
<repositories>
  <!-- Other repositories may be here too -->
  <repository>
    <id>UncertWebMavenRepository</id>
    <name>UncertWeb Maven Repository</name>
    <url>http://giv-uw.uni-muenster.de/m2/repo</url>
  </repository>
</repositories>
```

The dependency for the interface can then be added.

```xml
<dependencies>
  <!-- Other dependencies may be here too -->
  <dependency>
    <groupId>org.uncertweb</groupId>
    <artifactId>landsfacts-interface</artifactId>
    <version>0.1</version>
  </dependency>
</dependencies>
```

## Usage

For each of the fields you wish to run the simulation on, create a `FieldDescription`. At the bare minimum, a field should have an integer ID, soil type (can be any string), and area.

```java
FieldDescription description = new FieldDescription(1, "loamy", 20000);
fieldDescriptions.add(description);
```

A set of initial crop probability transition matrices are also required - one transition matrix per soil type.

```java
InitialTransitionMatrix matrix = new InitialTransitionMatrix("loamy");
matrix.addRow("CropA", new Double[] { 0.6, 0.4 });
matrix.addRow("CropB", new Double[] { 0.2, 0.8 });
initialTransitionMatrices.add(matrix);
```

The sample code above represents the following transition matrix. In this matrix, the probability of a field with CropA transitioning to CropB is 0.4, and the probability of a field with CropB transitioning to CropA is 0.2.

```
          | CropA | CropB 
---------------------------
  CropA   |  0.6  |  0.4
  CropB   |  0.2  |  0.8
```

You can then create the project and run the simulation using the interface.

```java
// Create project
int numSimulations = 3; // this is the default
Project project = new Project(fieldDescriptions, initialTransitionMatrices, numSimulations);

// Create interface
LandsfactsInterface landsfactsInterface = new LandsfactsInterface("C:\\landsfacts\\LandSFACTS_2-0-4.exe");

// Run
List<CropAllocation> cropAllocations = landsfactsInterface.run(project);
```

The size of the output `cropAllocations` list will be equal to the number of simulations. A `CropAllocation` has contains a list of `Allocation` objects (one per field).

```java
// Get first simulated set of allocations
CropAllocation allocation = cropAllocations.get(0);

// Get allocation for first field
Allocation fieldAllocation = allocation.getAllocations().get(0);

// Print details
// LandSFACTS runs over a simulation period of 5 years
String[] crops = fieldAllocation.getCrops()
System.out.println("Crop allocations for field " + fieldAllocation.getFieldID());
System.out.println("Year 1 = " + crops[0]);
System.out.println("Year 2 = " + crops[1]);
System.out.println("Year 3 = " + crops[2]);
System.out.println("Year 4 = " + crops[3]);
System.out.println("Year 5 = " + crops[4]);
```

### Under OSX/Linux

If you wish to run LandSFACTS on OSX or Linux, you will need to build for these platforms. The source code is [available upon request](http://www.macaulay.ac.uk/LandSFACTS/download.php). A number minor changes to the code are required - some unnecessary Windows specific calls are present. I'll document these in the future, or potentially upload the modified source.

## Build

If you wish to build the project from source, rename `landsfacts.sample.properties` in `src/test/resources` to `landsfacts.properties`. In this file, change the `executable.path` property to the location of the LandSFACTS executable on your local machine. To build:

```console
$ mvn clean package
```