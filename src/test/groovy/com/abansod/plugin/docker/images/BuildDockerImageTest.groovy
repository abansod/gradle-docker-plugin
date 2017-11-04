package com.abansod.plugin.docker.images

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class BuildDockerImageTest extends Specification {

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File dockerFile


    def setup() {
        buildFile = testProjectDir.newFile("build.gradle")
        def dockerFolder = testProjectDir.newFolder("docker")
        dockerFile = new File(dockerFolder, "Dockerfile")
    }

    def "shouldBuildDockerImage"() {
        given:
        dockerFile << """
        FROM java:8-jre
        MAINTAINER reports-team
        EXPOSE 8082
        """
        buildFile << """
        plugins {
                id 'com.abansod.plugin.docker'
            }
        import com.abansod.plugin.docker.images.BuildDockerImage
        task dockerBuildImage(type: BuildDockerImage){
        tags = ['localhost:5000/testbuild:build-ss']
        dockerFile = project.file('docker/Dockerfile')
        }
        """

        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('dockerBuildImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains('Created image successfully with Image ID :')

    }
}
