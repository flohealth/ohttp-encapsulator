package org.platform

internal object OHttpNativeWrapper {

    external fun init()
    external fun encapsulateRequest(config: ByteArray, message: ByteArray): Long
    external fun getEncapsulatedRequest(contextPointer: Long): ByteArray?
    external fun drop(contextPointer: Long)
    external fun decapsulateResponse(contextPointer: Long, encapsulatedResponse: ByteArray): ByteArray?
    external fun lastErrorMessage(): String?

    init {
        try {
            System.loadLibrary("apprelay")
            // initialize logging
            init()
        } catch (e: UnsatisfiedLinkError) {
            throw OHttpNativeWrapperInitializationException("Unable to load library", e)
        }
    }
}

internal fun OHttpNativeWrapper.lastErrorMessageOrUnknown() = lastErrorMessage() ?: "Unknown error"
