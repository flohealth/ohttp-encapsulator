package health.flo.network.ohttp.encapsulator

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OhttpEncapsulatorInstrumentedTest {

    @Test
    fun encapsulateRequest_ShouldReturn_EncapsulationResultFailure_whenJniLibIsLinked() {
        val request = "hello world".toByteArray()
        val invalidConfig = byteArrayOf(0, 0, 1)

        val encapsulated = ohttpEncapsulator().encapsulateRequest(request, invalidConfig)

        assertTrue(encapsulated is EncapsulationResult.Failure)
    }
}
