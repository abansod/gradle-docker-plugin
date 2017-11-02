package com.abansod.plugin.docker.images

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class PushDockerImageTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File dockerFile


    def setup() {
        buildFile = testProjectDir.newFile("build.gradle")
        def dockerFolder = testProjectDir.newFolder("docker")
        dockerFile = new File(dockerFolder, "Dockerfile")
    }

    def "shouldPushDockerImage"() {
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
        import com.akshay.plugin.docker.images.BuildDockerImage
        import com.akshay.plugin.docker.images.PushDockerImage
        task dockerBuildImage(type: BuildDockerImage){
        tags = ['localhost:5000/testbuild:build-ss']
        dockerFile = project.file('docker/Dockerfile')
        }
        task dockerPushImage(type: PushDockerImage){
        imageName = 'localhost:5000/testbuild:build-ss'
        tag = 'build-ss'
        }
        """

        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('dockerBuildImage','dockerPushImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains('Successfully pushed image: localhost:5000/testbuild:build-ss with build-ss')

    }
}
