package com.abansod.plugin.docker

import com.abansod.plugin.docker.images.BuildDockerImage
import com.abansod.plugin.docker.images.ListDockerImages
import com.abansod.plugin.docker.images.PushDockerImage
import com.abansod.plugin.docker.images.RemoveDockerImage
import com.abansod.plugin.docker.images.TagDockerImage
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task


class DockerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create('docker', DockerPluginExtension)

        project.afterEvaluate {
            def buildDockerImageTask = createBuildDockerImageTask(project)
            def pushDockerImageTask = createPushDockerImageTask(project)
            def removeDockerImage = createRemoveDockerImage(project)
        }
    }

    Task createBuildDockerImageTask(Project project) {
        DockerPluginExtension dockerExt = project.docker
        return project.task('buildDockerImage', type: BuildDockerImage) {
            if (dockerExt.inputDir) {
                inputDir = dockerExt.inputDir
            }
            if (dockerExt.dockerFile) {
                dockerFile = dockerExt.dockerFile
            }
            tags = ["$dockerExt.registry/$dockerExt.imageName:$dockerExt.tag".toString()]
        }
    }

    Task createPushDockerImageTask(Project project) {
        DockerPluginExtension dockerExt = project.docker
        return project.task('pushDockerImage', type: PushDockerImage){
            imageName = "$dockerExt.registry/$dockerExt.imageName:$dockerExt.tag".toString()
            tag = dockerExt.tag
        }
    }

    Task createRemoveDockerImage(Project project){
        DockerPluginExtension dockerExt = project.docker
        return project.task('removeDockerImage', type: RemoveDockerImage){
            imageId = "$dockerExt.registry/$dockerExt.imageName:$dockerExt.tag".toString()
        }
    }

}
