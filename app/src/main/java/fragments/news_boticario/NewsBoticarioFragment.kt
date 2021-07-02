package fragments.news_boticario

import adapters.NewsBoticarioAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.desafio_boticario.R
import models.NewsBoticario
import models.NewsBoticarioResponse
import rest.ApiClient
import rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.ConnectionDetector

class NewsBoticarioFragment : Fragment() {

    private var loadNewsBoticario: Call<NewsBoticarioResponse>? = null
    private var adapter: NewsBoticarioAdapter? = null
    private var newsBoticario = ArrayList<NewsBoticario>()

    lateinit var rvNews: RecyclerView
    lateinit var pbNewsBoticario: ProgressBar
    lateinit var txtNoNews: TextView
    lateinit var txtNoInternet: TextView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_boticario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbNewsBoticario = view.findViewById(R.id.pb_news_boticario)
        txtNoNews = view.findViewById(R.id.txt_no_news)
        txtNoInternet = view.findViewById(R.id.txt_no_internet)
        swipeRefreshLayout = view.findViewById(R.id.srl_news_boticario)

        swipeRefreshLayout.setColorSchemeResources(R.color.dark_purple)
        swipeRefreshLayout.setOnRefreshListener {
            getNews()
        }
        swipeRefreshLayout.isEnabled = true

        rvNews = view.findViewById(R.id.rv_news_boticario)
        rvNews.layoutManager = LinearLayoutManager(requireContext())
        rvNews.itemAnimator = DefaultItemAnimator()

        context?.let {
            adapter = NewsBoticarioAdapter(newsBoticario, it)
            rvNews.adapter = adapter
        }

    }

    override fun onStart() {
        super.onStart()
        val cd = ConnectionDetector(requireContext())
        if(!cd.isConnectingToInternet){
            pbNewsBoticario.visibility = View.GONE
            txtNoNews.visibility = View.GONE
            txtNoInternet.visibility = View.VISIBLE
            return
        } else {
            getNews()
        }
    }

    private fun getNews() {

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        loadNewsBoticario = apiService.getNewsBoticario()

        loadNewsBoticario?.enqueue(object : Callback<NewsBoticarioResponse> {
            override fun onResponse(call: Call<NewsBoticarioResponse>?, response: Response<NewsBoticarioResponse>?) {

                val news = response?.body()?.news

                if (news.isNullOrEmpty()) {
                    txtNoNews.visibility = View.VISIBLE
                } else {
                    newsBoticario.clear()
                    newsBoticario.addAll(news)
                    adapter?.notifyDataSetChanged()
                    txtNoNews.visibility = View.GONE
                    txtNoInternet.visibility = View.GONE
                }
                pbNewsBoticario.visibility = View.GONE

                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }

            }

            override fun onFailure(call: Call<NewsBoticarioResponse>?, t: Throwable?) {
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
                pbNewsBoticario.visibility = View.GONE
            }
        })

    }
}