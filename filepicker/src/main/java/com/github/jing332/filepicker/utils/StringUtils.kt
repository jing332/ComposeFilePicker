package com.github.jing332.filepicker.utils

import kotlin.math.ln
import kotlin.math.pow

object StringUtils {
    fun Long.sizeToReadable(): String {
        val bytes = this
        val unit = 1024
        if (bytes < unit) return "$bytes B"
        val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1] + "i"
        return String.format("%.1f %sB", bytes / unit.toDouble().pow(exp.toDouble()), pre)
    }
}