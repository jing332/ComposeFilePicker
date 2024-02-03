package com.github.jing332.filepicker.model

abstract class IFileModel {
    open val name: String = ""
    open val path: String = ""
    open val isDirectory: Boolean = false
    open val fileCount: Int = 0
    open val time: Long = 0
    open val size: Long = 0

    open fun createDirectory(name: String) {}
    open fun createFile(name: String) {}

    open fun files(): List<IFileModel> = emptyList()
}