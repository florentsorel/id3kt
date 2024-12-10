package dev.sorel.id3.id3v1

class ID3v1(
    private val content: ByteArray,
) {
    companion object {
        internal const val TAG_SIZE = 128
        internal const val SIGNATURE = "TAG"
    }

    val title = content.decodeToString(3, 33).trimEnd('\u0000')
    val artist = content.decodeToString(33, 63).trimEnd('\u0000')
    val album = content.decodeToString(63, 93).trimEnd('\u0000')
    val year = content.decodeToString(93, 97).trimEnd('\u0000')
    val track = if (hasTrack()) content[126].toInt() else null
    val comment =
        if (hasTrack()) {
            content.decodeToString(97, 124).trimEnd('\u0000')
        } else {
            content.decodeToString(97, 127).trimEnd('\u0000')
        }
    val genre = ID3v1Genre.fromCode(content[127].toInt())

    private fun hasTrack() = content[125].toInt() == 0 && content[126].toInt() != 0
}
