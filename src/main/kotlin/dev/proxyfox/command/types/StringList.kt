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
@Serializable(with = StringListSerializer::class)
public value class StringList(public val list: List<String>)

private class StringListSerializer : CommandSerializer<StringList> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("StringList", PrimitiveKind.STRING)

    override fun decodeCommand(decoder: CommandDecoder): StringList {
        val list = ArrayList<String>()
        while (!decoder.cursor.end)
            list.add(decoder.decodeString())
        return StringList(list)
    }

    override fun decodeRegular(decoder: Decoder): StringList = StringList(decoder.decodeSerializableValue(serializer<List<String>>()))

    override fun serialize(encoder: Encoder, value: StringList) {
        encoder.encodeSerializableValue(serializer<List<String>>(), value.list)
    }
}
