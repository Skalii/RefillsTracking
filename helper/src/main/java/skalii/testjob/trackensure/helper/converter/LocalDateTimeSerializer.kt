package skalii.testjob.trackensure.helper.converter


import java.time.LocalDateTime

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

import skalii.testjob.trackensure.helper.getDateTimeFormatter


@ExperimentalSerializationApi
@Serializer(forClass = LocalDateTime::class)
object LocalDateSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)


    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(getDateTimeFormatter()))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString(), getDateTimeFormatter())

}