![build status](https://travis-ci.org/abansod/gradle-docker-plugin.svg?branch=master)

# gradle-docker-plugin

Build script snippet for use in all Gradle versions:
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.abansod.plugin:gradle-docker-plugin:1.0.1"
  }
}


apply plugin: "com.abansod.plugin.docker"
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

```
plugins {
  id "com.abansod.plugin.docker" version "1.0.1"
}
```