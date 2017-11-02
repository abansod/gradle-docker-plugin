package com.abansod.plugin.docker

import com.abansod.plugin.docker.images.BuildDockerImage
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task


class DockerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create('docker', DockerPluginExtension)

        project.afterEvaluate {

            Task buildDockerImageTask = createBuildDockerImageTask(project)

        }
    }

    Task createBuildDockerImageTask(Project project) {
        DockerPluginExtension dockerExt = project.docker
        project.task('buildDockerImage', type: BuildDockerImage) {
            if (dockerExt.inputDir) {
                inputDir = dockerExt.inputDir
            }
            if (dockerExt.dockerFile) {
                dockerFile = dockerExt.dockerFile
            }
            tags = ["$dockerExt.registry/$dockerExt.imageName:$dockerExt.tag".toString()]

        }
    }
}
