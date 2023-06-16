package health.flo.network.ohttp.encapsulator

sealed class DecapsulationResult {

    class Success(
        val data: ByteArray,
    ) : DecapsulationResult()

    class Failure(
        val message: String,
        val additionalData: Map<String, Any> = emptyMap(),
    ) : DecapsulationResult()

    object Disposed : DecapsulationResult()
}
