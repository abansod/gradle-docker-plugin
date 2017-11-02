package com.abansod.plugin.docker.images

import com.abansod.plugin.docker.DockerClientFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class RemoveDockerImage extends DefaultTask {

    @Input
    String imageId
    @Input
    @Optional
    boolean forceRemove
    @Input
    @Optional
    boolean noPrune

    @TaskAction
    removeDockerImage() {
        logger.quiet("Removing docker image with image Id : '$imageId'")
        def dockerClient = DockerClientFactory.getDockerClient()

        def removeImageCmd = dockerClient.removeImageCmd(imageId)
                .withForce(forceRemove)
                .withNoPrune(noPrune)
                .exec()
        logger.quiet("Removed docker image : '$imageId'")
    }
}
