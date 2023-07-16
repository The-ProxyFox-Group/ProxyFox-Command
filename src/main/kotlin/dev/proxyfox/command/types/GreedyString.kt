package dev.proxyfox.command.types

import dev.proxyfox.command.CommandDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@JvmInline
@Serializable(with = GreedyStringSerializer::class)
public value class GreedyString(public val value: String)

private class GreedyStringSerializer : CommandSerializer<GreedyString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("GreedyString", PrimitiveKind.STRING)

    override fun decodeCommand(decoder: CommandDecoder): GreedyString = GreedyString(decoder.cursor.seekToEnd())

    override fun decodeRegular(decoder: Decoder): GreedyString = GreedyString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: GreedyString) {
        encoder.encodeString(value.value)
    }
}
