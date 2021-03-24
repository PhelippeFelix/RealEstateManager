package com.openclassrooms.realestatemanager.Controller.Views

import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list_item.view.*


class FragmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithData(estate: FullEstate, position:Int, lastItemClick:Int){
        val glide: RequestManager = Glide.with(itemView)
        val dollar = "$"

       if (position == lastItemClick){
           itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorAccentSelected))
       }else{
           itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorWhite))
       }

        if (estate.images.isNotEmpty()){
            glide.load(Uri.parse(estate.images[0].imagePath)).apply(RequestOptions().centerCrop()).into(itemView.list_item_main_pic)
        }else{
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions().centerCrop()).into(itemView.list_item_main_pic)
        }

        if(estate.estate.estateStatute == itemView.resources.getString(R.string.activity_add_estate_sold))
            glide.load(R.drawable.ic_sold).apply(RequestOptions().centerCrop()).into(itemView.image_sold)

        if (estate.estate.estateType.isNullOrEmpty()){
            itemView.list_item_type.text = itemView.resources.getString(R.string.list_fragment_no_type)
        }else{
            itemView.list_item_type.text = estate.estate.estateType.toString()
        }

       if (estate.location.city.isNullOrEmpty()){
           itemView.list_item_city.text = itemView.resources.getString(R.string.list_fragment_no_city)
       }else{
           itemView.list_item_city.text = "${estate.location.city}"
       }

        if (estate.estate.price == null){
            itemView.list_item_price.text = itemView.resources.getString(R.string.list_fragment_no_price)
        }else{
            itemView.list_item_price.text = dollar+"${estate.estate.price}"
        }


    }
}