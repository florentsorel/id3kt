package dev.sorel.id3

import dev.sorel.id3.exceptions.ID3v1Exception
import dev.sorel.id3.id3v1.ID3v1
import dev.sorel.id3.id3v2.ID3v2
import java.io.File
import java.nio.file.Path

class MP3File(
    private var content: ByteArray,
) {
    private var isID3v1: Boolean? = null
    private var isID3v2: Boolean? = null

    constructor(filename: String) : this(File(filename).readBytes())
    constructor(file: File) : this(file.readBytes())
    constructor(path: Path) : this(path.toFile().readBytes())

    init {
        isID3v1 = isID3v1(content)
        isID3v2 = isID3v2(content)
    }

    private fun isID3v1(bytes: ByteArray): Boolean {
        val lastBytes = bytes.takeLast(ID3v1.TAG_SIZE).toByteArray()
        if (bytes.size < ID3v1.TAG_SIZE) throw ID3v1Exception("File is too small to be an ID3v1 tag")
        return lastBytes.decodeToString().take(3) == ID3v1.SIGNATURE
    }

    private fun isID3v2(bytes: ByteArray) = bytes.take(3).toByteArray().decodeToString() == ID3v2.SIGNATURE

    fun getID3v1(): ID3v1? =
        if (isID3v1 == true) {
            ID3v1(content.takeLast(ID3v1.TAG_SIZE).toSignedByteArray())
        } else {
            null
        }

    private fun List<Byte>.toSignedByteArray() = map { it.toInt() and 0xFF }.map { it.toByte() }.toByteArray()
}
