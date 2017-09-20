package pl.osmalek.bartek.rxbootcamp

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainPresenter {

    private var view: MainView? = null

    private var disposable: Disposable? = null

    private val service = WordpressService()

    fun onViewCreated(view: MainView) {
        this.view = view
    }

    fun onSearchWatchReady(searchObservable: Observable<CharSequence>) {
        disposable = searchObservable
                .debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext(::println)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { this.view?.showLoading() }
                .observeOn(Schedulers.io())
                .getPosts()
                .observeOn(AndroidSchedulers.mainThread())
                .handleError()
                .doOnNext { this.view?.hideLoading() }
                .subscribe { it ->
                    if (it.isNotEmpty()) {
                        this.view?.showResults(it)
                    } else {
                        this.view?.showNoResults()
                    }
                }
    }

    fun onViewDestroyed() {
        view = null
        disposable?.dispose()
    }

    private fun Observable<List<PostWithAuthor>>.handleError(): Observable<List<PostWithAuthor>> {
        return doOnError { view?.showError() }
                .onErrorReturn { listOf() }
    }

    private fun <T : CharSequence> Observable<T>.getPosts() =
            switchMapSingle { if (it.isNotBlank()) service.getPosts(it) else Single.just(listOf()) }

}
