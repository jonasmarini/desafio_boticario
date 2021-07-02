package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio_boticario.R
import constants.Constants
import de.hdodenhof.circleimageview.CircleImageView
import entities.PostsEntity
import utils.formatDateToString

class PostsAdapter(private val posts: ArrayList<PostsEntity>, private val context: Context,
                   private var recyclerView: RecyclerView, private var userId: Long): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_ITEM = 1
        const val VIEW_PROG = 0
    }

    private var visibleThreshold = 6
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var loading = false

    private var listener: PostsListener? = null

    init {
        if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    totalItemCount = linearLayoutManager.itemCount
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (listener != null) {
                            listener!!.onLoadMore()
                        }
                        loading = true
                    }
                }
            })
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

        if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    totalItemCount = linearLayoutManager.itemCount
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (listener != null) {
                            listener!!.onLoadMore()
                        }
                        loading = true
                    }
                }
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (posts[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder?
        if (viewType == VIEW_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.posts_item, parent, false)
            viewHolder = PostsViewHolder(view, parent.context, listener)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progressbar_item_table_row, parent, false)
            viewHolder = ProgressViewHolder(view)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostsViewHolder).bindView(posts[position], context, userId)
        if (holder is PostsViewHolder) {
            val post = posts[position]
            if (post != null) {
                holder.bindView(post, context, userId)
            }
        } else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoaded() {
        loading = false
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    interface PostsListener {
        fun onButtonClicked(position: Int, type: String)
        fun onLoadMore()
    }

    fun setListener(listener: PostsListener) {
        this.listener = listener
    }

    class PostsViewHolder(itemView: View, private val context: Context, private val listener: PostsListener?): RecyclerView.ViewHolder(itemView){
        fun bindView(post: PostsEntity, context: Context, userId: Long){

            val author = itemView.findViewById<TextView>(R.id.txt_author_news_boticario)
            val image = itemView.findViewById<CircleImageView>(R.id.img_news_boticario)
            val date = itemView.findViewById<TextView>(R.id.txt_date_news_boticario)
            val message = itemView.findViewById<TextView>(R.id.txt_message_news_boticario)
            val imgEdit = itemView.findViewById<ImageView>(R.id.img_edit_post)
            val imgDelete = itemView.findViewById<ImageView>(R.id.img_delete_post)

            if(post.userId == userId){

                imgEdit.visibility = View.VISIBLE
                imgEdit.setOnClickListener {
                    listener?.onButtonClicked(adapterPosition, Constants.EDIT)
                }

                imgDelete.visibility = View.VISIBLE
                imgDelete.setOnClickListener {
                    listener?.onButtonClicked(adapterPosition, Constants.DELETE)
                }

            } else {
                imgEdit.visibility = View.GONE
                imgDelete.visibility = View.GONE
            }

            author.text = post.author

            Glide
                .with(context)
                .load(R.drawable.logo_flower)
                .into(image)

            message.text = post.message
            date.text = post.created_date?.formatDateToString()

        }
    }

    class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.pb_load_more)

    }
}