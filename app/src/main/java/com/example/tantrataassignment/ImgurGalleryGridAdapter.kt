package com.example.tantrataassignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tantrataassignment.databinding.GridCellBinding
import com.example.tantrataassignment.model.Data

//For grid view
class ImgurGalleryGridAdapter(val data: List<Data>) :
    RecyclerView.Adapter<ImgurGalleryGridAdapter.MyViewHolder>() {

    class MyViewHolder(private val itemViewCellBinding: GridCellBinding) :
        RecyclerView.ViewHolder(itemViewCellBinding.root) {

        fun bind(data: Data) {
            itemViewCellBinding.tvTitle.text = data.title
            itemViewCellBinding.tvDate.text = MainActivity.getFormattedDate(data.datetime * 1000)

            if (data.images != null && data.images.isNotEmpty()) {
                itemViewCellBinding.tvImageCount.text = data.images.size.toString()

                Glide.with(itemViewCellBinding.tvDate.context)
                    .load("https://i.imgur.com/${data.images[0].id}.jpeg")
                    .placeholder(R.drawable.place_holder)
                    .into(itemViewCellBinding.ivImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViewCellBinding =
            GridCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemViewCellBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        if (data != null)
            return data.size
        return 0
    }
}