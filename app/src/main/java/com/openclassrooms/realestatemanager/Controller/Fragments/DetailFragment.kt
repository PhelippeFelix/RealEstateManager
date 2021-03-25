package com.openclassrooms.realestatemanager.Controller.Fragments

import androidx.lifecycle.Observer
import android.content.Context
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.openclassrooms.realestatemanager.Controller.Activities.EditActivity
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.Image

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import kotlinx.android.synthetic.main.fragment_detail.*
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.utils.toFRString
import io.reactivex.disposables.Disposable


class DetailFragment : BaseFragment(), ActivityAddAdapter.Listener, OnMapReadyCallback {

    private lateinit var mViewModel:EstateViewModel
    private var databaseId:Any? = null
    private lateinit var listImages:ArrayList<Image>
    private lateinit var adapter:ActivityAddAdapter
    private lateinit var mainDesc:String

    private lateinit var googleMap: GoogleMap
    private lateinit var mapView:MapView
    override var disposable: Disposable? = null

    companion object {
        fun newInstance():DetailFragment{
            return DetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(this.requireContext())).get(EstateViewModel::class.java)

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detail_fragment_map.onCreate(savedInstanceState)
        detail_fragment_map.getMapAsync(this)

        this.retrieveDatabaseId()
        this.configureFABOnClickListener()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
        this.configureRestoreDescButton()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.uiSettings.isCompassEnabled = false
        this.googleMap.uiSettings.isMyLocationButtonEnabled = false
        this.googleMap.uiSettings.isMapToolbarEnabled = false
        this.googleMap.uiSettings.setAllGesturesEnabled(false)
        this.googleMap.setOnMapClickListener {  }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.disposeWhenDestroy()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureRecyclerView(){
        this.listImages = ArrayList()
        this.adapter = ActivityAddAdapter(this.listImages,this, Constants.VIEW_HOLDER_ACTION_DETAIL)
        detail_fragment_recycler_view.adapter = this.adapter
        detail_fragment_recycler_view.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun configureRestoreDescButton(){
        detail_fragment_restore_desc.setOnClickListener { detail_fragment_desc.text = mainDesc }
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(detail_fragment_recycler_view,R.layout.activity_add_item).setOnItemClickListener{
            _: RecyclerView?, position: Int, _: View? ->
                detail_fragment_desc.text = adapter.getImageDesc(position)
        }
    }

    private fun retrieveDatabaseId(){
        databaseId = arguments?.get(DATABASE_ID)
        Log.e("DETAIL_FRAGMENT","Id retrieved : $databaseId")
        if(databaseId != null){
            mViewModel.getEstatesByID(databaseId as Long).observe(viewLifecycleOwner, Observer {
                updateUI(it!!)
                val address = getFullAddressFromEstate(it.location)
                if (address != null)executeHttpRequestWithRetrofit(address,databaseId as Long)
            })
        }
    }

    private fun configureFABOnClickListener(){
        detail_fragment_fab.setOnClickListener {
            if (databaseId != null){
                val map = hashMapOf(DATABASE_ID to databaseId as Long)
                (activity as MainActivity).launchActivity(((activity as MainActivity).applicationContext),EditActivity::class.java,map)
            }
        }
    }

    // ---------------------
    // UI
    // ---------------------

    override fun getPositionFromRxRequest(position: LatLng?, estateId: Long) {
        val markerOptions = MarkerOptions()
        if (position != null){
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, Constants.DETAIL_FRAGMENT_DEFAULT_ZOOM))
            markerOptions.icon(getMarkerIconFromDrawable())
            markerOptions.position(position)
            val marker = this.googleMap.addMarker(markerOptions)
            marker.tag = estateId
        }

    }

    private fun updateUI(result:FullEstate){
        listImages.clear()

        this.checkAndDisplayResultData(result)

        adapter.notifyDataSetChanged()
    }

    private fun checkAndDisplayResultData(result: FullEstate){
        if(result.estate.desc.isNullOrEmpty()){
            mainDesc = resources.getString(R.string.detail_fragment_no_desc)
            detail_fragment_desc.text = resources.getString(R.string.detail_fragment_no_desc)
        }else{
            mainDesc = result.estate.desc!!
            detail_fragment_desc.text = result.estate.desc
        }

        detail_fragment_added_on.text = resources.getString(R.string.detail_fragment_added_on,result.estate.entryDate.toFRString())
        if(result.estate.soldDate != null ) detail_fragment_sold_on.text = resources.getString(R.string.detail_fragment_sold_on, result.estate.soldDate!!.toFRString())

        if (result.estate.surface.toString() == "null"){
            detail_fragment_surface.text = resources.getString(R.string.detail_fragment_not_specified)
        }else{
            detail_fragment_surface.text = result.estate.surface.toString()
        }

        if (result.estate.roomNumber.toString() == "null"){
            detail_fragment_rooms.text = resources.getString(R.string.detail_fragment_not_specified)
        }else{
            detail_fragment_rooms.text = result.estate.roomNumber.toString()
        }

        if (result.estate.bathroomNumber.toString() == "null"){
            detail_fragment_bathrooms.text = resources.getString(R.string.detail_fragment_not_specified)
        }else{
            detail_fragment_bathrooms.text = result.estate.bathroomNumber.toString()
        }

        if (result.estate.bedroomNumber.toString() == "null"){
            detail_fragment_bedrooms.text = resources.getString(R.string.detail_fragment_not_specified)
        }else{
            detail_fragment_bedrooms.text = result.estate.bedroomNumber.toString()
        }

        if (isAddressComplete(result)){
            detail_fragment_location_address.text = result.location.address
            detail_fragment_location_add_address.text = result.location.additionalAddress
            detail_fragment_location_city.text = result.location.city
            detail_fragment_location_zip.text = result.location.zipCode
            detail_fragment_location_country.text = result.location.country
            detail_fragment_map.visibility = View.VISIBLE
        }else{
            detail_fragment_location_address.text = resources.getString(R.string.detail_fragment_not_specified)
            detail_fragment_map.visibility = View.GONE
        }

        setImage(requireContext(), park_image,result.estate.parks)
        setImage(requireContext(), shop_image,result.estate.shops)
        setImage(requireContext(), school_image,result.estate.schools)
        setImage(requireContext(), highway_image,result.estate.highway)

        if(result.images.isEmpty()){
            detail_fragment_recycler_view.visibility = View.GONE
            detail_fragment_no_image_available.visibility = View.VISIBLE
        }else{
            detail_fragment_recycler_view.visibility = View.VISIBLE
            detail_fragment_no_image_available.visibility = View.GONE
            listImages.addAll(result.images)
        }
    }

    private fun isAddressComplete(result: FullEstate) =
            !result.location.address.isNullOrEmpty() &&
                    !result.location.city.isNullOrEmpty() &&
                    !result.location.zipCode.isNullOrEmpty() &&
                    !result.location.country.isNullOrEmpty()

    // Need to be min SDK 21 to add the theme variable on the constructor to be a more actual way to do it and not deprecated
    @Suppress("DEPRECATION")

    private fun setImage(context: Context, imageView: ImageView, result:Boolean){
          try {
              lateinit var icon:Drawable
              lateinit var filter:LightingColorFilter
              val colorGreen = ContextCompat.getColor(context, R.color.colorGreen)
              val colorRed = ContextCompat.getColor(context, R.color.colorRed)
               if (result){
                      icon = context.resources.getDrawable(R.drawable.baseline_check_black_18)
                      filter = LightingColorFilter(colorGreen, colorGreen)
                  }else{
                      icon = context.resources.getDrawable(R.drawable.baseline_clear_black_18)
                      filter = LightingColorFilter(colorRed, colorRed)
                  }

              icon.colorFilter = filter
              imageView.setImageDrawable(icon)
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }
    override fun onClickDeleteButton(position: Int) {}
}
