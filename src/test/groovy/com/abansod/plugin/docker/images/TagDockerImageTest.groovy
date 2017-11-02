package com.abansod.plugin.docker.images

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class TagDockerImageTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File dockerFile

    def setup() {
        buildFile = testProjectDir.newFile("build.gradle")
        def dockerFolder = testProjectDir.newFolder("docker")
        dockerFile = new File(dockerFolder, "Dockerfile")
    }

    def "shouldTagDockerImage"() {
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
        import com.akshay.plugin.docker.images.TagDockerImage
        
        task dockerBuildImage(type: BuildDockerImage){
        tags = ['localhost:5000/testimage:build-xxx']
        dockerFile = project.file('docker/Dockerfile')
        }
        
        task tagDockerImage(type: TagDockerImage){
        imageId = 'localhost:5000/testimage:build-xxx'
        repository = 'localhost:5000/test_tag_image'
        tag = 'build-xxx'
        }
        """
        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('dockerBuildImage', 'tagDockerImage')
                .withPluginClasspath()
                .build()
        then:
        results.output.contains("tagged image localhost:5000/testimage:build-xxx with localhost:5000/test_tag_image:build-xxx successfully")
    }
}
