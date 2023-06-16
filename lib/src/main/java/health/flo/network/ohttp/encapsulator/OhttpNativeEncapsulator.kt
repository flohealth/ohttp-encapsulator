package health.flo.network.ohttp.encapsulator

import org.platform.OHttpNativeWrapper
import org.platform.lastErrorMessageOrUnknown

internal object OhttpNativeEncapsulator : OhttpEncapsulator {

    /**
     * Takes a serialized http request
     * @param data serialized http request
     * @param config public key obtained from gateway server
     * @return possible bytes containing encrypted user request that should be sent to relay server
     */
    override fun encapsulateRequest(data: ByteArray, config: OhttpCryptoConfig): EncapsulationResult {
        val pointer = OHttpNativeWrapper.encapsulateRequest(config, data)

        if (pointer == -1L) {
            val message = OHttpNativeWrapper.lastErrorMessageOrUnknown()
            return EncapsulationResult.Failure(message)
        }

        val result = OHttpNativeWrapper.getEncapsulatedRequest(pointer)
        if (result == null) {
            val message = OHttpNativeWrapper.lastErrorMessageOrUnknown()
            return EncapsulationResult.Failure(message)
        }

        return EncapsulationResult.Success(
            result,
            OhttpNativeDecapsulator(pointer),
        )
    }
}
