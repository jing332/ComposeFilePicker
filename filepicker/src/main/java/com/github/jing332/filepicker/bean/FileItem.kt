package com.github.jing332.filepicker.bean

class FileItem(
    override var name: String,
    override var path: String,
    var isChecked: Boolean,
    var isDirectory: Boolean,
    var size: Long,
    var lastModified: Long,
) : IFile {
}