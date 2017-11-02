package com.abansod.plugin.docker.images

import com.abansod.plugin.docker.DockerClientFactory
import com.github.dockerjava.api.model.Image
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class ListDockerImages extends DefaultTask {

    @Optional
    @Input
    Closure<List<Image>> responseHandler

    @Optional
    @Input
    Boolean showAll

    @Optional
    @Input
    String imageName

    @Optional
    @Input
    String[] labels

    @TaskAction
    void listDockerImages() {
        def dockerClient = DockerClientFactory.dockerClient
        def cmd = dockerClient.listImagesCmd()

        if (showAll) {
            cmd.withShowAll(true)
        }
        if (imageName) {
            cmd.withImageNameFilter(imageName)
        }
        if (labels) {
            cmd.withLabelFilter(labels)
        }

        List<Image> listOfImages = cmd.exec()

        if (responseHandler) {
            responseHandler.call(listOfImages)
        } else {
            listOfImages.forEach({image ->
                logger.quiet "Repository Tags : ${image.repoTags?.join(', ')}"
                logger.quiet "Image ID        : $image.id"
                logger.quiet "Created         : ${new Date(image.created * 1000)}"
                logger.quiet "Virtual Size    : $image.virtualSize"
                logger.quiet "-----------------------------------------------"
            })
        }
    }

}
