package dev.proxyfox.command.types

import dev.proxyfox.command.CommandDecoder
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

@JvmInline
@Serializable(with = UnixListSerializer::class)
public value class UnixList(public val list: List<String>) {
    override fun toString(): String {
        return list.toString()
    }
}

private class UnixListSerializer : CommandSerializer<UnixList> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UnixList", PrimitiveKind.STRING)

    override fun decodeCommand(decoder: CommandDecoder): UnixList {
        val list = ArrayList<String>()
        while (!decoder.cursor.end) {
            decoder.cursor.checkout()
            val string = decoder.decodeString()
            if (!string.startsWith("-")) {
                decoder.cursor.rollback()
                break
            }
            list.add(string.replace(Regex("--?"), ""))
            decoder.cursor.commit()
        }
        return UnixList(list)
    }

    override fun decodeRegular(decoder: Decoder): UnixList = UnixList(decoder.decodeSerializableValue(serializer<List<String>>()))

    override fun serialize(encoder: Encoder, value: UnixList) {
        encoder.encodeSerializableValue(serializer<List<String>>(), value.list)
    }
}
