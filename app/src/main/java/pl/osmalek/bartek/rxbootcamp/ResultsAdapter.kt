package pl.osmalek.bartek.rxbootcamp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class ResultsAdapter : RecyclerView.Adapter<ResultVH>() {
    private var posts: List<PostWithAuthor> = listOf()

    override fun onBindViewHolder(holder: ResultVH, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ResultVH(view)
    }

    fun setData(posts: List<PostWithAuthor>) {
        this.posts = posts
        notifyDataSetChanged()
    }
}