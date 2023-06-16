package health.flo.network.ohttp.encapsulator

sealed class EncapsulationResult {

    class Success(
        val data: ByteArray,
        val decapsulator: OhttpDecapsulator,
    ) : EncapsulationResult()

    class Failure(
        val message: String,
        val additionalData: Map<String, Any> = emptyMap(),
    ) : EncapsulationResult()
}
