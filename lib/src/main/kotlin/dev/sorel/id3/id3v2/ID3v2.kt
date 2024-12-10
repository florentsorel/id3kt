package dev.sorel.id3.id3v2

class ID3v2(
    private val content: ByteArray,
) {
    companion object {
        internal const val SIGNATURE = "ID3"
    }
}
