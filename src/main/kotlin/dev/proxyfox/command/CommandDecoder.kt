package dev.proxyfox.command

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.UNKNOWN_NAME
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

public class CommandDecodingException(idx: Int, public val reason: String? = null) : Exception("Cannot decode command at $idx${reason?.let {": $it" } ?: ""}")

public inline fun <reified T> decode(cursor: StringCursor, context: CommandContext<Any>, serializer: KSerializer<T>): T =
    CommandDecoder(cursor, context).decodeSerializableValue(serializer)
public inline fun <reified T> decode(cursor: StringCursor, context: CommandContext<Any>): T =
    decode(cursor, context, serializer())

@OptIn(ExperimentalSerializationApi::class)
public class CommandDecoder(public val cursor: StringCursor, public val context: CommandContext<Any>) : AbstractDecoder() {
    private var elementsCount = 0

    override val serializersModule: SerializersModule = EmptySerializersModule()

    public fun fails(): Nothing = throw CommandDecodingException(cursor.index)
    public fun fails(reason: String): Nothing = throw CommandDecodingException(cursor.index, reason)

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementsCount == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementsCount++
    }

    override fun decodeString(): String {
        if (cursor.end) fails()
        val string = cursor.extractString(true)
        cursor.inc()
        return string
    }

    override fun decodeChar(): Char {
        cursor.checkout()
        val string = decodeString()
        if (string.length != 1) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return string[0]
    }

    override fun decodeBoolean(): Boolean {
        cursor.checkout()
        val string = cursor.extractString(false).lowercase()
        cursor.inc()
        if (string != "true" && string != "false"){
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return string == "true"
    }

    override fun decodeLong(): Long {
        cursor.checkout()
        val num = cursor.extractString(false).toLongOrNull()
        cursor.inc()
        if (num == null) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return num
    }

    override fun decodeInt(): Int {
        cursor.checkout()
        val num = cursor.extractString(false).toIntOrNull()
        cursor.inc()
        if (num == null) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return num
    }

    override fun decodeShort(): Short {
        cursor.checkout()
        val num = cursor.extractString(false).toShortOrNull()
        cursor.inc()
        if (num == null) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return num
    }

    override fun decodeByte(): Byte {
        cursor.checkout()
        val num = cursor.extractString(false).toByteOrNull()
        cursor.inc()
        if (num == null) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return num
    }

    override fun decodeDouble(): Double {
        cursor.checkout()
        val num = cursor.extractString(false).toDoubleOrNull()
        cursor.inc()
        if (num == null) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return num
    }

    override fun decodeFloat(): Float {
        cursor.checkout()
        val num = cursor.extractString(false).toFloatOrNull()
        cursor.inc()
        if (num == null) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return num
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        cursor.checkout()
        val idx = enumDescriptor.getElementIndex(decodeString())
        if (idx == UNKNOWN_NAME) {
            cursor.rollback()
            fails()
        }
        cursor.commit()
        return idx
    }

    override fun decodeNotNullMark(): Boolean {
        return false
    }

    override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T? {
        return try {
            deserializer.deserialize(this)
        } catch (err: CommandDecodingException) {
            null
        }
    }

    override fun decodeSequentially(): Boolean = true
}
