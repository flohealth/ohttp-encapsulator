package health.flo.network.ohttp.encapsulator

/**
 * Interface for native jni interface created in
 * https://github.com/cloudflare/privacy-gateway-client-library
 */
interface OhttpEncapsulator {
    /**
     * Takes a serialized http request
     * @param data serialized http request
     * @param config public key obtained from gateway server
     * @return possible bytes containing encrypted user request that should be sent to relay server
     */
    fun encapsulateRequest(data: ByteArray, config: OhttpCryptoConfig): EncapsulationResult
}

fun ohttpEncapsulator(): OhttpEncapsulator = OhttpNativeEncapsulator
