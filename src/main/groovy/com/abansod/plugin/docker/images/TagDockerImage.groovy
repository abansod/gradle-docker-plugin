package com.abansod.plugin.docker.images

import com.abansod.plugin.docker.DockerClientFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class TagDockerImage extends DefaultTask {

    @Input
    String imageId
    @Input
    String repository
    @Input
    String tag
    @Input
    @Optional
    Boolean force

    void targetImageId(Closure targetImageId) {
        imageId = targetImageId
    }

    @TaskAction
    tagDockerImage() {
        logger.quiet "tagging image $imageId with $repository:$tag"
        def dockerClient = DockerClientFactory.getDockerClient()
        def tagImageCmd = dockerClient.tagImageCmd(imageId, repository, tag)
                .withForce(force)
                .exec()
        logger.quiet "tagged image $imageId with $repository:$tag successfully"

    }

}
