package fragments.news_boticario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.desafio_boticario.R
import models.NewsBoticarioResponse
import rest.ApiClient
import rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsBoticarioFragment : Fragment() {

    var loadNewsBoticario: Call<NewsBoticarioResponse>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_boticario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        print("Caiu aqui")
    }

    override fun onStart() {
        super.onStart()
        print("Caiu aqui")
        getNews()
    }

    private fun getNews() {

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        loadNewsBoticario = apiService.getNewsBoticario()

        loadNewsBoticario?.enqueue(object : Callback<NewsBoticarioResponse> {
            override fun onResponse(call: Call<NewsBoticarioResponse>?, response: Response<NewsBoticarioResponse>?) {
                val body = response!!.body()!!
                print("Caiu aqui")
            }

            override fun onFailure(call: Call<NewsBoticarioResponse>?, t: Throwable?) {
                print("Caiu aqui")
            }
        })

    }
}