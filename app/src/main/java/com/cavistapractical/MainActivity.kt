package com.cavistapractical

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cavistapractical.adapter.SearchAdapter
import com.cavistapractical.model.Image
import com.cavistapractical.presenter.SearchPresenter
import com.cavistapractical.view.SearchView
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response


class MainActivity : AppCompatActivity(), SearchView.MainView,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var presenter: SearchPresenter
    private lateinit var searchList: ArrayList<Image>
    private lateinit var adapter: SearchAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var text = ""
    private var page = 1
    private var isLoading = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        presenter = SearchPresenter(this, this)
        searchList = arrayListOf()
        layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        adapter = SearchAdapter(searchList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        searchView.setOnQueryTextListener(this)
        adapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(view: View, obj: Image, position: Int) {
                startActivity(
                    Intent(this@MainActivity, MainActivity2::class.java).putExtra("data", obj)
                )
            }
        })
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener)
    }

    // region Listeners
    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading) {

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        page++
                        presenter.loadData(page, text)
                    }
                }
            }
        }

    private fun removeListeners() {
        recyclerView.removeOnScrollListener(recyclerViewOnScrollListener)
    }

    override fun showProgressbar() {
        if (page == 1) {
            progressBar.visibility = View.VISIBLE
            progressBar1.visibility = View.GONE
        } else {
            isLoading = true
            progressBar.visibility = View.GONE
            progressBar1.visibility = View.VISIBLE
        }
    }

    override fun hideProgressbar() {
        isLoading = false
        progressBar.visibility = View.GONE
        progressBar1.visibility = View.GONE
    }

    override fun onSuccess(responseModel: Response<JsonObject>) {
        if (responseModel.body() != null) {
            val dataArray = responseModel.body()!!.getAsJsonArray("data")
            for (i in 0 until dataArray.size()) {
                val dataObject = dataArray[i].asJsonObject
                if (dataObject.has("images")) {
                    val title = dataObject.get("title").asString
                    val imageArray = dataObject.getAsJsonArray("images")
                    for (j in 0 until imageArray.size()) {
                        val imageObject = imageArray[j].asJsonObject
                        val type = imageObject.get("type").asString
                        val id = imageObject.get("id").asString
                        val link = imageObject.get("link").asString
                        if (TextUtils.equals(type, "image/jpeg")
                            || TextUtils.equals(type, "image/png")
                        ) {
                            val image = Image()
                            image.id = id
                            image.title = title
                            image.link = link
                            searchList.add(image)
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onError(errorCode: Int) {
        when (errorCode) {
            500 -> {
                page--
                Toast.makeText(this, "Server error", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                page--
                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onError(throwable: Throwable) {
        page--
        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchList.clear()
        adapter.notifyDataSetChanged()
        text = query
        page = 1
        presenter.loadData(page, query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
        removeListeners()
    }
}