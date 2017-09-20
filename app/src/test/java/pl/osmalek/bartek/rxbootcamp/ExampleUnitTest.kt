package pl.osmalek.bartek.rxbootcamp

import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun initials() {
        val testObserver = getInitials("Bartłomiej Osmałek").test()
        testObserver.assertValue("B.O.")
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun initialsLowerCase() {
        val testObserver = getInitials("bartłomiej osmałek").test()
        testObserver.assertValue("B.O.")
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun initialsDoubleSpace() {
        val testObserver = getInitials("bartłomiej  osmałek").test()
        testObserver.assertValue("B.O.")
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun initials3Words() {
        val testObserver = getInitials("bartłomiej osmałek kk").test()
        testObserver.assertValue("B.O.K.")
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun initialsWithEmptyString() {
        val testObserver = getInitials("").test()
        testObserver.assertValue("")
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    private fun getInitials(name: String): Single<String> {
        return Single.just(name)
                .flatMapObservable { Observable.fromIterable(it.split(' ')) }
                .filter { it.isNotBlank() }
                .map { it.first() }
                .collect({ StringBuilder() }, { builder, char -> builder.append("${char.toUpperCase()}.") })
                .map { it.toString() }
    }
}
