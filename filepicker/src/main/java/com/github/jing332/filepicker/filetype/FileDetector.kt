package com.github.jing332.filepicker.filetype

import com.github.jing332.filepicker.model.IFileModel

class FileDetector {
    val allDefaultFileTypes by lazy {
        setOf(
            AudioType,
            TextType,
            ImageType,
            ApkType,
            CodeType,
            DatabaseType,
            ArchiveType,
        )
    }

    private val fileTypes by lazy {
        mutableSetOf<IFileType>().apply { addAll(allDefaultFileTypes) }
    }

    fun clearFileType() {
        fileTypes.clear()
    }

    fun addFileType(fileType: IFileType) {
        fileTypes.add(fileType)
    }

    fun removeFileType(fileType: IFileType) {
        fileTypes.remove(fileType)
    }

    fun detect(model: IFileModel): IFileType? {
        fileTypes.forEach {
            if (it.verify(model)) {
                return it
            }
        }

        return null
    }
}