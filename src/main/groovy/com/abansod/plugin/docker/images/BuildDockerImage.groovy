package com.abansod.plugin.docker.images

import com.abansod.plugin.docker.DockerClientFactory
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.BuildResponseItem
import com.github.dockerjava.core.command.BuildImageResultCallback
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

class BuildDockerImage extends DefaultTask {

    /**
     * Input directory containing the build context. Defaults to "$projectDir/docker".
     */
    @InputDirectory
    File inputDir = project.file('docker')

    /**
     * The Dockerfile to use to build the image.  If null, will use 'Dockerfile' in the
     * build context, i.e. "$inputDir/Dockerfile".
     */
    @InputFile
    @Optional
    File dockerFile

    /**
     * Tags for image.
     */
    @Input
    @Optional
    Set<String> tags = []

    @Input
    @Optional
    Boolean noCache

    @Input
    @Optional
    Boolean remove = true

    @Input
    @Optional
    Boolean quiet

    @Input
    @Optional
    Boolean pull

    @Input
    @Optional
    Map<String, String> labels = [:]

    @Input
    @Optional
    Map<String, String> buildArgs = [:]

    @Input
    @Optional
    boolean forceRemove


    @TaskAction
    def dockerBuildImage() {

        logger.quiet "creating imgage with '$tags'"
        DockerClient dockerClient = DockerClientFactory.getDockerClient()
        def buildImageCmd = dockerClient.buildImageCmd()
                .withBaseDirectory(inputDir)
                .withRemove(remove)
                .withNoCache(noCache)
                .withPull(pull)
                .withQuiet(quiet)
                .withForcerm(forceRemove)

        if (dockerFile) {
            buildImageCmd = buildImageCmd.withDockerfile(dockerFile)
        } else {
            buildImageCmd = buildImageCmd.withDockerfile(new File(inputDir, "Dockerfile"))
        }

        if (getTags()) {
            buildImageCmd = buildImageCmd.withTags(tags)
        }
        if (getLabels()) {
            buildImageCmd.withLabels(getLabels())
        }
        buildArgs.each { arg, value ->
            buildImageCmd = buildImageCmd.withBuildArg(arg, value)
        }
        ResultCallback<BuildResponseItem> callback = new BuildImageResultCallback()
        def imageId = buildImageCmd.exec(callback).awaitImageId()
        logger.quiet("Created image successfully with Image ID :'$imageId'")

    }

}
