package pl.osmalek.bartek.rxbootcamp

interface MainView {
    fun showResults(results: List<PostWithAuthor>)
    fun showLoading()
    fun hideLoading()
    fun showNoResults()
    fun showError()
}