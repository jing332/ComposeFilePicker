package com.github.jing332.filepicker.model

abstract class IFileModel {
    open val name: String = ""
    open val path: String = ""
    open val isDirectory: Boolean = false
    open val fileCount: Int = 0
    open val time: Long = 0
    open val size: Long = 0

    open val isChecked: Boolean = false

    open fun files(): List<IFileModel> = emptyList()
}