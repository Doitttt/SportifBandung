package com.nyonz.sportifbandung

import Facility
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class FacilityFragment : Fragment() {

    private lateinit var facilityAdapter: FacilityAdapter
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_facility, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_facility)
        recyclerView.layoutManager = LinearLayoutManager(context)

        facilityAdapter = FacilityAdapter(listOf(), requireContext()) { facility ->
            val intent = Intent(activity, FacilityDetailActivity::class.java)
            intent.putExtra("facility", facility)
            startActivity(intent)
        }
        recyclerView.adapter = facilityAdapter

        fetchFacilities()

        return view
    }

    private fun fetchFacilities() {
        db.collection("1").get()
            .addOnSuccessListener { result ->
                val facilities = result.map { document ->
                    Facility(
                        id = document.id,
                        nama_prasarana_olahraga = document.getString("nama_prasarana_olahraga") ?: "",
                        alamat = document.getString("alamat") ?: "",
                        cabang_olahraga = document.getString("cabang_olahraga") ?: ""
                    )
                }
                facilityAdapter.updateFacilities(facilities)
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}
