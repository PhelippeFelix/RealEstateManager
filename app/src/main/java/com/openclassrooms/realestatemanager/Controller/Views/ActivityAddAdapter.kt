package com.openclassrooms.realestatemanager.Controller.Views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R


class ActivityAddAdapter(private val pictures:List<Image>, val callback:Listener, val action:String) : RecyclerView.Adapter<ActivityAddViewHolder>() {

    var imageDetails:Boolean = false

    interface Listener{
        fun onClickDeleteButton(position:Int)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ActivityAddViewHolder {
       return ActivityAddViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.activity_add_item, p0, false))
    }

    override fun getItemCount(): Int {
       return pictures.size
    }

    fun imageGetDetails(position: Int){
        imageDetails = true
        notifyItemChanged(position)
    }

    fun getImageDesc(position: Int): String {
        return pictures[position].imageDesc!!
    }

    override fun onBindViewHolder(p0: ActivityAddViewHolder, p1: Int) {
        p0.updateWithData(this.pictures[p1],this.callback, this.action)
        imageDetails = false
    }
}