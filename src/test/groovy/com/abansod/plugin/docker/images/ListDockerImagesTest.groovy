package com.abansod.plugin.docker.images

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class ListDockerImagesTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File dockerFile


    def setup() {
        buildFile = testProjectDir.newFile("build.gradle")
        def dockerFolder = testProjectDir.newFolder("docker")
        dockerFile = new File(dockerFolder, "Dockerfile")
    }

    def "ListDockerImages"() {
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
        import com.akshay.plugin.docker.images.ListDockerImages
        task dockerBuildImage(type: BuildDockerImage){
        tags = ['localhost:5000/testbuild:build-ss']
        dockerFile = project.file('docker/Dockerfile')
        }
        task dockerListImage(type: ListDockerImages){
        }
        """

        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('dockerBuildImage','dockerListImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains('localhost:5000/testbuild:build-ss')

    }


    def "ListDockerImagesWithHandler"() {
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
        import com.akshay.plugin.docker.images.ListDockerImages
        task dockerBuildImage(type: BuildDockerImage){
        tags = ['localhost:5000/testbuild:build-ss']
        dockerFile = project.file('docker/Dockerfile')
        }
        task dockerListImage(type: ListDockerImages){
            responseHandler = { images -> images.forEach{ image -> println image.repoTags }}
        }
        """

        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('dockerBuildImage','dockerListImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains('localhost:5000/testbuild:build-ss')

    }

}
