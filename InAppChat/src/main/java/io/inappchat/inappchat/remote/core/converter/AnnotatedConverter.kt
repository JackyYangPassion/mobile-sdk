package io.inappchat.inappchat.remote.core.converter

import retrofit2.Converter
import io.inappchat.inappchat.remote.core.converter.AnnotatedConverter
import retrofit2.Retrofit
import okhttp3.ResponseBody
import okhttp3.RequestBody
import java.lang.NullPointerException
import java.lang.reflect.Type
import java.util.LinkedHashMap
import kotlin.reflect.KClass

class AnnotatedConverter private constructor(factories: Map<KClass<out Annotation>, Converter.Factory>) :
    Converter.Factory() {
    private val factories: Map<KClass<out Annotation>, Converter.Factory>

    class Builder {
        private val factories: MutableMap<KClass<out Annotation>, Converter.Factory> =
            LinkedHashMap()

        fun add(cls: KClass<out Annotation>?, factory: Converter.Factory?): Builder {
            if (cls == null) {
                throw NullPointerException("cls == null")
            }
            if (factory == null) {
                throw NullPointerException("factory == null")
            }
            factories[cls] = factory
            return this
        }

        fun build(): AnnotatedConverter {
            return AnnotatedConverter(factories)
        }
    }

    init {
        this.factories = LinkedHashMap(factories)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            val factory = factories[annotation.annotationClass]
            if (factory != null) {
                return factory.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        for (annotation in parameterAnnotations) {
            val factory = factories[annotation.annotationClass]
            if (factory != null) {
                return factory.requestBodyConverter(
                    type, parameterAnnotations, methodAnnotations, retrofit
                )
            }
        }
        return null
    }
}