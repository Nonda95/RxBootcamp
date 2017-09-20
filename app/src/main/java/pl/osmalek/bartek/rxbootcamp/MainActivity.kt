package pl.osmalek.bartek.rxbootcamp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {
    private val presenter = MainPresenter()

    private val adapter = ResultsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareRecyclerView()

        presenter.onViewCreated(this, etSearch.textChanges())
    }

    private fun prepareRecyclerView() {
        rvResults.layoutManager = LinearLayoutManager(this)
        rvResults.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun showResults(results: List<PostWithAuthor>) {
        hideNoResultsText()
        adapter.setData(results)
    }

    override fun showNoResults() {
        adapter.setData(listOf())
        if (etSearch.text.isNotEmpty()) {
            showNoResultsText()
        } else {
            hideNoResultsText()
        }
    }

    private fun showNoResultsText() {
        tvNoResults.visibility = View.VISIBLE
    }

    private fun hideNoResultsText() {
        tvNoResults.visibility = View.GONE
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
        ivError.visibility = View.GONE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.INVISIBLE
    }

    override fun showError() {
        ivError.visibility = View.VISIBLE
    }
}

