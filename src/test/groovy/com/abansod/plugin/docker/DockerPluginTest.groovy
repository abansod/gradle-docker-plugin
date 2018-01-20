package com.abansod.plugin.docker

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class DockerPluginTest extends Specification {

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File dockerFile


    def setup() {
        buildFile = testProjectDir.newFile("build.gradle")
        def dockerFolder = testProjectDir.newFolder("docker")
        dockerFile = new File(dockerFolder, "Dockerfile")
    }

    def "shouldBuildDockerImageUsingBuildDockerImageTask"() {
        given:
        dockerFile << """
        FROM java:8-jre
        MAINTAINER reports-team
        EXPOSE 8082
        """
        buildFile << """
        plugins {
                id 'com.abansod.plugin.docker' version '1.0.0'
            }
        docker{
            imageName = 'test_image'
            registry = 'localhost:5000'
            tag = 'build-xxx'
        }
        """
        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('buildDockerImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains('Created image successfully with Image ID :')
    }

    def "shouldPushDockerImageUsingBuildDockerImageTask"() {
        given:
        dockerFile << """
        FROM java:8-jre
        MAINTAINER reports-team
        EXPOSE 8082
        """
        buildFile << """
        plugins {
                id 'com.abansod.plugin.docker' version '1.0.0'
            }
        docker{
            imageName = 'test_image'
            registry = 'localhost:5000'
            tag = 'build-xxx'
        }
        """
        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('buildDockerImage','pushDockerImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains('Successfully pushed image: localhost:5000/test_image:build-xxx with build-xxx')

    }

    def "shouldRemoveDockerImageUsingBuildDockerImageTask"() {
        given:
        dockerFile << """
        FROM java:8-jre
        MAINTAINER reports-team
        EXPOSE 8082
        """
        buildFile << """
        plugins {
                id 'com.abansod.plugin.docker' version '1.0.0'
            }
        docker{
            imageName = 'test_image'
            registry = 'localhost:5000'
            tag = 'build-xxx'
        }
        """
        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('buildDockerImage','removeDockerImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains("Removed docker image : 'localhost:5000/test_image:build-xxx'")
    }
}
