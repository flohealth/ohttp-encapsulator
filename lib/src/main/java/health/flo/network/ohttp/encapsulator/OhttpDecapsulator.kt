package health.flo.network.ohttp.encapsulator

/**
 * Wrapper around native pointer to encapsulated request context
 * providing access to decapsulation operation
 */
interface OhttpDecapsulator {
    /**
     * Decrypts the response from the relay server and returns the original response from resource server.
     * This will clean up the allocated memory. No need to call `dispose`
     *
     * @param data serialized http response body from relay server
     * @return possible decrypted http response from target resource server.
     *         still needs to be deserialized
     */
    fun decapsulateResponse(data: ByteArray): DecapsulationResult

    /**
     * Disposes the request context and frees up allocated memory
     * This should be called in case of an error if the server
     * fails to produce a response (e.g. server is down)
     */
    fun dispose(): Boolean
}
