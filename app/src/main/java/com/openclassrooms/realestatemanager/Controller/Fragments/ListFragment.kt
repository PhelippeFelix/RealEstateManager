package com.openclassrooms.realestatemanager.Controller.Fragments

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.FragmentListAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.FullEstate

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.DividerItemDecoration
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import kotlinx.android.synthetic.main.fragment_list.*


const val DATABASE_ID = "DATABASE_ID"

class ListFragment : Fragment() {

    private lateinit var listEstate: ArrayList<FullEstate>
    private lateinit var adapter:FragmentListAdapter
    private lateinit var mViewModel: EstateViewModel

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("ListFragment", "Displaying fragment...")
        mViewModel = ViewModelProvider(this,Injection.provideViewModelFactory(this.requireContext())).get(EstateViewModel::class.java)

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments?.size()
        if (bundle != null) {
            val query = requireArguments().getString("QUERY")
            val args = requireArguments().getStringArrayList("ARGS") as ArrayList<Any>
            mViewModel.getEstatesBySearch(query!!,args).observe(viewLifecycleOwner, Observer { updateUI(it!!) })
        }else{
            mViewModel.getEstates().observe(viewLifecycleOwner, Observer<List<FullEstate>> { updateUI(it!!) })
        }

        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------
    private fun configureRecyclerView(){
        this.listEstate = ArrayList()
        this.adapter = FragmentListAdapter(this.listEstate)
        this.fragment_list_recycler_view.adapter = this.adapter
        this.fragment_list_recycler_view.layoutManager = LinearLayoutManager(activity)
        fragment_list_recycler_view.addItemDecoration(DividerItemDecoration(activity as Activity,0, 0))
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(fragment_list_recycler_view, R.layout.fragment_list_item)
                .setOnItemClickListener{ _, position, _ ->
                    if ((activity as MainActivity).isTablet()){
                        adapter.setBackgroundColorItemClicked(position)
                    }
                    this.launchDetailFragment(adapter.getEstateInfos(position).estate.id)
        }
    }

    // ---------------------
    // ACTION
    // ---------------------

    private fun launchDetailFragment(databaseId : Long){
        val bundle = Bundle()
        val newFragment = DetailFragment.newInstance()
        bundle.putLong(DATABASE_ID,databaseId)
        newFragment.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        if((activity as MainActivity).isTablet()){
            transaction.replace(R.id.fragment_view_detail, newFragment)
        }else{
            transaction.replace(R.id.fragment_view, newFragment)
        }

        transaction.addToBackStack(null)
        transaction.commit()

    }

    // ---------------------
    // UI
    // ---------------------

    private fun updateUI(results:List<FullEstate>){
        this.listEstate.clear()
        this.listEstate.addAll(results)
        adapter.notifyDataSetChanged()
    }

}
