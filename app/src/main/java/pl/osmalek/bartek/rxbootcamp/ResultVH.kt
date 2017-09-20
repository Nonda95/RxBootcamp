package pl.osmalek.bartek.rxbootcamp

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item.view.*

class ResultVH(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(post: PostWithAuthor) {
        with(itemView) {
            title.text = post.title
            author.text = post.author
        }
    }
}