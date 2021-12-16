<<<<<<< HEAD:app/src/androidTest/java/uk/ac/shef/oak/com4510/ExampleInstrumentedTest.kt
package uk.ac.shef.oak.com4510
=======
package uk.ac.shef
>>>>>>> Member1PrivateWork:app/src/androidTest/java/uk/ac/shef/ExampleInstrumentedTest.kt

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lab.lab5_kt", appContext.packageName)
    }
}