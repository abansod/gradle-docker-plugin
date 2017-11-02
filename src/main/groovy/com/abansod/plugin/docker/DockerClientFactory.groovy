package com.abansod.plugin.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientBuilder

class DockerClientFactory {

    static DockerClient dockerClient = buildDockerClient()

    private static DockerClient buildDockerClient() {
        def builder = DefaultDockerClientConfig.createDefaultConfigBuilder()
        DockerClientBuilder.getInstance(builder).build()
    }

}
