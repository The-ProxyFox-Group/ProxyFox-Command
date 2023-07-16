package dev.proxyfox.command.types

import dev.proxyfox.command.CommandDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder

public interface CommandSerializer<T> : KSerializer<T> {
    public fun decodeCommand(decoder: CommandDecoder): T

    public fun decodeRegular(decoder: Decoder): T

    override fun deserialize(decoder: Decoder): T {
        return if (decoder is CommandDecoder)
            decodeCommand(decoder)
        else decodeRegular(decoder)
    }
}