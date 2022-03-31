[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/baudoliver7/jdbc-toolset)](http://www.rultor.com/p/baudoliver7/jdbc-toolset)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Javadoc](http://www.javadoc.io/badge/com.baudoliver7/jdbc-toolset.svg)](http://www.javadoc.io/doc/com.baudoliver7/jdbc-toolset)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/baudoliver7/jdbc-toolset/blob/master/LICENSE.txt)
[![codecov](https://codecov.io/gh/baudoliver7/jdbc-toolset/branch/master/graph/badge.svg)](https://codecov.io/gh/baudoliver7/jdbc-toolset)
[![Hits-of-Code](https://hitsofcode.com/github/baudoliver7/jdbc-toolset)](https://hitsofcode.com/view/github/baudoliver7/jdbc-toolset)
[![Maven Central](https://img.shields.io/maven-central/v/com.baudoliver7/jdbc-toolset.svg)](https://maven-badges.herokuapp.com/maven-central/com.baudoliver7/jdbc-toolset)
[![PDD status](http://www.0pdd.com/svg?name=baudoliver7/jdbc-toolset)](http://www.0pdd.com/p?name=baudoliver7/jdbc-toolset)

A toolset for `Jdbc`

There is some tools :

## DataSource and Connection wrappers

We give some wrappers to easily decorate `DataSource` and `Connection`.

```java
public final MyDataSource extends DataSourceWrap {
    
    public MyDataSource(final DataSource origin) {
        ...
    }
}

public final MyConnection extends ConnectionWrap {

    public MyConnection(final Connection origin) {
    ...
    }
}
``` 
## LockedConnection: A connection which can never be closed

Sometimes, we don't want some pieces of code to close your connection after use. So, to prevent
them to close your connection, you can decorate it with `LockedConnection` before give them
like this:

```java
new LockedConnection(
    connection
)
```
## LocalLockedDataSource: a DataSource that gives only one connection per thread

Sometimes, we are in situations where we want to use only one connection during the current thread
and be sure that all modifications are taken into account only when we decide to explicitly commit
them. Then, `LocalLockedDataSource` is your friend in such case. Just decorate your datasource like
this :

```java
final ThreadLocal<Connection> cthread = new ThreadLocal<>();
final DataSource uds = new LocalLockedDataSource(
    datasource, cthread
);

final Connection conn1 = uds.getConnection();
// We do here some operations with our connection.
// After that, we attempt to commit and close it.
conn1.commit(); // no effect
conn1.close(); // no effect
    ...
// Somewhere else in the same thread, we want a connection
// to do another operations.
final Connection conn2 = uds.getConnection(); // we get here the current connection
    ...
    ...
// We choose now to commit all changes.
// For that, we should use connection that is stored in the local thread `cthread`.
cthread.get().commit();

// After that, trying to get a connection will return a new connection for the current thread.
final Connection conn3 = uds.getConnection();
```

## Use it in your project

If you're using Maven, you should add it to your <code>pom.xml</code> dependencies:

```xml
<dependency>
    <groupId>com.baudoliver7</groupId>
    <artifactId>jdbc-toolset</artifactId>
    <version><!-- latest version --></version>
</dependency>
``` 

## How to contribute

Fork repository, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

> mvn clean install -Pqulice

Keep in mind that JDK 8 and Maven 3.1.0 are the lowest versions you may use.

## Got questions ?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/baudoliver7/jdbc-toolset/issues/new).