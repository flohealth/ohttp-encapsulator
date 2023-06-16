package health.flo.network.ohttp.encapsulator

import health.flo.network.ohttp.encapsulator.DecapsulationResult.Disposed
import health.flo.network.ohttp.encapsulator.DecapsulationResult.Failure
import health.flo.network.ohttp.encapsulator.DecapsulationResult.Success
import org.platform.OHttpNativeWrapper
import org.platform.lastErrorMessageOrUnknown
import java.util.concurrent.atomic.AtomicBoolean

internal class OhttpNativeDecapsulator(
    private val pointer: Long,
) : OhttpDecapsulator {

    /** guard against double free */
    private val disposedAtomic = AtomicBoolean(false)

    /**
     * Decrypts the response from the relay server and returns the original response from resource server.
     * This will clean up the allocated memory. No need to call `dispose`
     *
     * @param data serialized http response body from relay server
     * @return possible decrypted http response from target resource server.
     *         still needs to be deserialized
     */
    override fun decapsulateResponse(data: ByteArray): DecapsulationResult {
        if (disposedAtomic.compareAndSet(false, true)) {
            val response = OHttpNativeWrapper.decapsulateResponse(pointer, data)

            if (response == null) {
                val message = OHttpNativeWrapper.lastErrorMessageOrUnknown()
                return Failure(message)
            }

            return Success(response)
        } else {
            return Disposed
        }
    }

    /**
     * Disposes the request context and frees up allocated memory.
     * This should be called in case of an error if the server fails to produce a response (e.g. server is down).
     */
    override fun dispose(): Boolean {
        return if (disposedAtomic.compareAndSet(false, true)) {
            OHttpNativeWrapper.drop(pointer)
            true
        } else {
            // The context must have been already disposed before
            // possibly by another thread calling @see decapsulateResponse
            val alreadyDisposed = IllegalStateException("Already disposed")
            alreadyDisposed.printStackTrace()
            false
        }
    }
}
