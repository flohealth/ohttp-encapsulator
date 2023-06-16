package org.platform

internal class OHttpNativeWrapperInitializationException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
