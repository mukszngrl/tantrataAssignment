package com.example.tantrataassignment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tantrataassignment.api.ImgurAPI
import com.example.tantrataassignment.api.RetrofitHelper
import com.example.tantrataassignment.databinding.ActivityMainBinding
import com.example.tantrataassignment.model.GalleryResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isToggled = true
    private var isGrid = false
    private lateinit var imgurAPI: ImgurAPI
    lateinit var galleryResponse: GalleryResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Layout reference by ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Creating API Interface reference for API call
        imgurAPI = RetrofitHelper.getInstance().create(ImgurAPI::class.java)

        getInitialList()

        binding.ivToggle.setOnClickListener {
            if (isToggled) {
                isToggled = false
                isGrid = true
                setGrid()
            } else {
                isToggled = true
                isGrid = false
                setList()
            }
        }

        binding.btnSearch.setOnClickListener {
            val searchText = binding.etSearchBox.text.toString()
            if (searchText.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.tvNoItem.visibility = View.VISIBLE
                    binding.tvNoItem.text = getString(R.string.loading)
                    binding.rvImgurGallery.visibility = View.GONE
                    val result = CoroutineScope(Dispatchers.IO).async {
                        imgurAPI.getGalleryDataBySearch(
                            sort = "top", window = "week", page = 1, searchText = searchText
                        )
                    }
                    galleryResponse = result.await()
                    if (isGrid) setGrid()
                    else setList()
                }
            }
        }

        binding.etSearchBox.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                getInitialList()
            }
        }
    }

    private fun getInitialList() {
        //Coroutine to launch is Main thread
        CoroutineScope(Dispatchers.Main).launch {
            binding.tvNoItem.visibility = View.VISIBLE
            binding.tvNoItem.text = getString(R.string.loading)
            binding.rvImgurGallery.visibility = View.GONE

            //Coroutine to launch is IO thread as async to get the result
            val result = CoroutineScope(Dispatchers.IO).async {
                imgurAPI.getGalleryData(
                    section = "top",
                    sort = "top",
                    window = "week",
                    page = 1,
                    showViral = true,
                    mature = true,
                    album_previews = true
                )
            }
            galleryResponse = result.await()

            // Setting List or Grid according to toggle
            if (isGrid) setGrid()
            else setList()
        }
    }

    // Setting List
    private fun setList() {
        binding.ivToggle.setImageResource(R.drawable.view_grid)
        if (galleryResponse.data.isNotEmpty()) {
            binding.tvNoItem.visibility = View.GONE
            binding.rvImgurGallery.visibility = View.VISIBLE
            binding.rvImgurGallery.layoutManager = LinearLayoutManager(
                this@MainActivity, LinearLayoutManager.HORIZONTAL, false
            )
            binding.rvImgurGallery.adapter = ImgurGalleryListAdapter(galleryResponse.data)
        } else {
            binding.tvNoItem.visibility = View.VISIBLE
            binding.tvNoItem.text = getString(R.string.no_item_found)
            binding.rvImgurGallery.visibility = View.GONE
        }
    }

    // Setting Grid
    private fun setGrid() {
        binding.ivToggle.setImageResource(R.drawable.view_list)
        if (galleryResponse.data.isNotEmpty()) {
            binding.tvNoItem.visibility = View.GONE
            binding.rvImgurGallery.visibility = View.VISIBLE
            binding.rvImgurGallery.layoutManager =
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            binding.rvImgurGallery.adapter = ImgurGalleryGridAdapter(galleryResponse.data)
        } else {
            binding.tvNoItem.visibility = View.VISIBLE
            binding.tvNoItem.text = getString(R.string.no_item_found)
            binding.rvImgurGallery.visibility = View.GONE
        }
    }

    //get formatted date in cell
    companion object {
        fun getFormattedDate(millis: Long): String {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
            return simpleDateFormat.format(millis)
        }
    }
}