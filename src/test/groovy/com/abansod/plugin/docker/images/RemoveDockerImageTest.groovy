package com.abansod.plugin.docker.images

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class RemoveDockerImageTest extends Specification {
    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File dockerFile

    def setup() {
        buildFile = testProjectDir.newFile("build.gradle")
        def dockerFolder = testProjectDir.newFolder("docker")
        dockerFile = new File(dockerFolder, "Dockerfile")
    }

    def "shouldRemoveDockerImage"() {
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
        import com.abansod.plugin.docker.images.RemoveDockerImage
        
        task dockerBuildImage(type: BuildDockerImage){
        tags = ['localhost:5000/testimage:build-xxx']
        dockerFile = project.file('docker/Dockerfile')
        }
        
        task removeDockerImage(type: RemoveDockerImage){
        imageId = 'localhost:5000/testimage:build-xxx'
        }
        """
        when:
        def results = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('dockerBuildImage','removeDockerImage')
                .withPluginClasspath()
                .build()

        then:
        results.output.contains("Removed docker image : 'localhost:5000/testimage:build-xxx'")
    }
}
