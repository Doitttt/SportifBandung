package com.nyonz.sportifbandung

import Booking
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingFragment : Fragment() {

    private lateinit var recyclerViewBookings: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        recyclerViewBookings = view.findViewById(R.id.recycler_view_bookings)
        recyclerViewBookings.layoutManager = LinearLayoutManager(context)

        // Dapatkan referensi ke database
        val database = FirebaseDatabase.getInstance()
        val dbRef = database.getReference("bookings")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bookings = mutableListOf<Booking>()
                for (snapshot in dataSnapshot.children) {
                    try {
                        val booking = snapshot.getValue(Booking::class.java)
                        if (booking != null) {
                            bookings.add(booking)
                        } else {
                            Log.e("BookingFragment", "Booking is null for snapshot: ${snapshot.key}")
                        }
                    } catch (e: Exception) {  // Tangkap semua jenis exception
                        Log.e("BookingFragment", "Error converting snapshot to Booking: ${e.message}")
                        Log.e("BookingFragment", "Snapshot key: ${snapshot.key}, value: ${snapshot.value}")
                    }
                }
                bookingAdapter = BookingAdapter(bookings)
                recyclerViewBookings.adapter = bookingAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("BookingFragment", "Database error: ${databaseError.message}")
            }
        })


        return view
    }
}
