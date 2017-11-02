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
                id 'com.akshay.plugin.docker'
            }
        docker{
        imageName = 'test_image'
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
}
