package com.nyonz.sportifbandung

import Booking
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter
    private val bookings = listOf(
        Booking("John Doe", "2024-08-15 10:00 AM", "123-456-7890"),
        Booking("Jane Smith", "2024-08-16 02:00 PM", "987-654-3210")
        // Tambahkan data pemesanan lainnya di sini
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        recyclerView = findViewById(R.id.recycler_view_bookings)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookingAdapter = BookingAdapter(bookings)
        recyclerView.adapter = bookingAdapter
    }
}
