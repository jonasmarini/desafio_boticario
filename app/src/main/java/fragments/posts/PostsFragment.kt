package fragments.posts

import adapters.PostsAdapter
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.desafio_boticario.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.orm.query.Condition
import com.orm.query.Select
import constants.Constants
import entities.PostsEntity
import entities.UserEntity
import java.util.*

class PostsFragment : Fragment() {

    private var adapter: PostsAdapter? = null
    private var listPosts = ArrayList<PostsEntity>()

    lateinit var rvPosts: RecyclerView
    lateinit var pbPosts: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    var userLogged: UserEntity? = null

    private var offset = 0
    private val loadPerPage = 15

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserLogged()

        pbPosts = view.findViewById(R.id.pb_posts)
        swipeRefreshLayout = view.findViewById(R.id.srl_posts)

        swipeRefreshLayout.setColorSchemeResources(R.color.dark_purple)
        swipeRefreshLayout.setOnRefreshListener {
            offset = 0
            listPosts.clear()
            loadPosts()
        }
        swipeRefreshLayout.isEnabled = true

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_post)
        fab.setOnClickListener {
            showDialogNewPost()
        }

        rvPosts = view.findViewById(R.id.rv_posts)
        rvPosts.layoutManager = LinearLayoutManager(requireContext())
        rvPosts.itemAnimator = DefaultItemAnimator()

        context?.let {
            userLogged?.let { user ->
                adapter = PostsAdapter(listPosts, it, rvPosts, user.id)
                rvPosts.adapter = adapter
            }
        }

        adapter?.setListener(object : PostsAdapter.PostsListener {

            override fun onButtonClicked(position: Int, type: String) {
                val postEdit = listPosts[position]

                if (Constants.EDIT == type) {
                    showDialogEditPost(postEdit)
                } else {
                    showDialogDeletePost(postEdit)
                }
            }

            override fun onLoadMore() {
                loadPosts()
            }
        })
    }

    private fun getUserLogged() {

        val sharedPreferences =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val userId = sharedPreferences.getLong(Constants.USER_LOGGED_ID, 0)

        userLogged = Select.from(UserEntity::class.java)
            .where(Condition.prop("id").eq(userId))
            .first()
    }

    override fun onStart() {
        super.onStart()
        loadPosts()
    }

    private fun loadPosts() {

        val posts = Select.from(PostsEntity::class.java)
            .limit("$offset,$loadPerPage")
            .orderBy("id")
            .list()

        if (!posts.isNullOrEmpty()) {
            listPosts.addAll(posts)
            adapter?.notifyDataSetChanged()
            offset += posts.size

            adapter?.setLoaded()
        }

        pbPosts.visibility = View.GONE

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showDialogDeletePost(editPostEntity: PostsEntity) {

        val builder = Builder(context)

        builder.setTitle(getString(R.string.delete_post_title))
        builder.setMessage(getString(R.string.delete_post_description))

        builder.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, _ ->
            deletePost(editPostEntity)
            dialog.dismiss()
        }

        builder.setNegativeButton(
            getString(R.string.no)
        ) { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun showDialogEditPost(editPostEntity: PostsEntity) {

        val viewDialog = Dialog(requireContext())
        viewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        viewDialog.setCancelable(true)
        viewDialog.setContentView(R.layout.dialog_new_post)
        viewDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title = viewDialog.findViewById<TextView>(R.id.txt_dialog)
        val editPost = viewDialog.findViewById<EditText>(R.id.edit_new_post)
        val btConfirm = viewDialog.findViewById<TextView>(R.id.bt_confirm_new_post)
        val btCancel = viewDialog.findViewById<TextView>(R.id.bt_cancel_new_post)

        editPost.setText(editPostEntity.message)

        title.text = getString(R.string.edit_post)

        btConfirm.setOnClickListener {
            if (editPost.text.toString().trim().isNotEmpty()) {
                editPostEntity.message = editPost.text.toString()
                saveEditPost(editPostEntity)
                viewDialog.dismiss()
            }
        }

        btCancel.setOnClickListener {
            viewDialog.dismiss()
        }

        viewDialog.show()
    }

    private fun showDialogNewPost() {

        val viewDialog = Dialog(requireContext())
        viewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        viewDialog.setCancelable(true)
        viewDialog.setContentView(R.layout.dialog_new_post)
        viewDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val title = viewDialog.findViewById<TextView>(R.id.txt_dialog)
        val editNewPost = viewDialog.findViewById<EditText>(R.id.edit_new_post)
        val btConfirm = viewDialog.findViewById<TextView>(R.id.bt_confirm_new_post)
        val btCancel = viewDialog.findViewById<TextView>(R.id.bt_cancel_new_post)

        title.text = getString(R.string.new_post)

        btConfirm.setOnClickListener {
            if (editNewPost.text.toString().trim().isNotEmpty()) {
                saveNewPost(editNewPost.text.toString())
                viewDialog.dismiss()
            }
        }

        btCancel.setOnClickListener {
            viewDialog.dismiss()
        }

        viewDialog.show()
    }

    private fun deletePost(deletePost: PostsEntity) {
        listPosts.remove(deletePost)
        deletePost.delete()
        adapter?.notifyDataSetChanged()
    }

    private fun saveEditPost(editPost: PostsEntity) {
        editPost.save()
        adapter?.notifyDataSetChanged()
    }

    private fun saveNewPost(message: String) {
        userLogged?.let {
            val post = PostsEntity(it.name, message, Date(), it.id)
            post.save()
            listPosts.add(post)
            adapter?.notifyDataSetChanged()
        }
    }
}