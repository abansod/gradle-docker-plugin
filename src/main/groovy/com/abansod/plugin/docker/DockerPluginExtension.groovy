package com.abansod.plugin.docker


class DockerPluginExtension {
    String imageName
    String registry = 'localhost:5000'
    String registryGCE = ''
    String tag = 'latest'
    File inputDir
    File dockerFile
}
