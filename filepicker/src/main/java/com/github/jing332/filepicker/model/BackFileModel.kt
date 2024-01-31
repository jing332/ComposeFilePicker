package com.github.jing332.filepicker.model

class BackFileModel : IFileModel() {
    override val name: String
        get() = "··"
    override val path: String
        get() = "../"
    override val isDirectory: Boolean
        get() = true
    override val fileCount: Int
        get() = 0
    override val time: Long
        get() = 0
    override val size: Long
        get() = 0
}