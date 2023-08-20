package com.example.smartmessenger

import java.io.File

fun processFilesInDirectory(directoryPath: String) {
    val directory = File(directoryPath)
    val files = directory.listFiles { file -> file.isFile && file.name.endsWith(".kt") }

    files?.forEach { file ->
        val originalContent = file.readText()
        val newContent = originalContent.replace("test smartmessage", "dolphi")

        if (newContent != originalContent) {
            file.writeText(newContent)
            println("Modified content in file: ${file.name}")
        }
    }
}

fun main() {
    val targetDirectory = "C:\\Users\\danil\\AndroidStudioProjects\\Dolphi\\app\\src\\main" // Укажите свой путь к целевой папке
    processFilesInDirectory(targetDirectory)
}
