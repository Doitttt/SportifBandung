package com.nyonz.sportifbandung

import Booking
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(
    private val bookings: List<Booking>
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount(): Int = bookings.size

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_booking_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.text_booking_date)
        private val contactTextView: TextView = itemView.findViewById(R.id.text_booking_contact)

        fun bind(booking: Booking) {
            nameTextView.text = booking.name
            dateTextView.text = booking.date
            contactTextView.text = booking.contact
        }
    }
}
