# Oblivious HTTP Encapsulator

The library provides a simple Kotlin API for Android apps to encapsulate and
decapsulate [Oblivious HTTP](https://datatracker.ietf.org/doc/html/draft-ietf-ohai-ohttp-02) requests to
the [Privacy Relay](https://github.com/cloudflare/privacy-gateway-relay).
The library represents an Android JNI wrapper
for [Cloudflare Privacy Gateway Client Library](https://github.com/cloudflare/privacy-gateway-client-library) binaries.

## Download

#### Declare Gradle dependencies

```kotlin
dependencies {
    implementation("com.github.flohealth:ohttp-encapsulator:0.1.0")
}
```

#### Download artifacts

You can download artifacts from [GitHub Releases](https://github.com/flohealth/ok-ohttp-encapsulator/releases). <br />

## Usage

```kotlin
import health.flo.network.ohttp.encapsulator.ohttpEncapsulator

val serializedRequest: ByteArray // serialized request in format used by your app
val cryptoConfig: ByteArray // OHTTP crypto config obtained from your server

val encapsulator: OhttpEncapsulator = ohttpEncapsulator()
val encapsulationResult: EncapsulationResult = encapsulator.encapsulateRequest(
    serializedRequest,
    cryptoConfig,
)

when (encapsulationResult) {
    is EncapsulationResult.Failure -> {
        // handle error
    }
    is EncapsulationResult.Success -> {
        val encapsulatedRequest: ByteArray = encapsulationResult.data
        val decapsulator: OhttpDecapsulator = encapsulationResult.decapsulator

        // send encryptedRequest to relay server & get encrypted response from relay server
        val encapsulatedResponse: ByteArray // encrypted response from relay

        val decapsulationResult: DecapsulationResult = decapsulator.decapsulateResponse(encapsulatedResponse)
        when (decapsulationResult) {
            is DecapsulationResult.Failure -> {
                // handle error
            }
            DecapsulationResult.Disposed -> {
                // this could happen if you call decapsulateResponse second time. 
                // you should call decapsulateResponse only once.
            }
            is DecapsulationResult.Success -> {
                val decapsulatedSerializedResponse: ByteArray = decapsulationResult.data
                // deserialize decryptedResponse into used response format & use it
            }
        }
    }
}
```

## License

Released under [**MIT License**](LICENSE.txt).
