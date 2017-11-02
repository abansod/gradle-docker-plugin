package com.abansod.plugin.docker.images

import com.abansod.plugin.docker.DockerClientFactory
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.command.PushImageResultCallback
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class PushDockerImage extends DefaultTask {

    @Input
    String imageName

    @Input
    @Optional
    String tag

    @TaskAction
    void pushDockerImage() {
        logger.quiet "Pushing image :{}", imageName
        DockerClient dockerClient = DockerClientFactory.getDockerClient()
        def pushImageCmd = dockerClient.pushImageCmd(imageName)
        if (tag) {
            pushImageCmd = pushImageCmd.withTag(tag)
        }
        pushImageCmd
                .exec(new PushImageResultCallback()).awaitSuccess()
        logger.quiet "Successfully pushed image: $imageName with $tag"
    }
}
