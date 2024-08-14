package com.nyonz.sportifbandung

import Booking
import Facility
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.FirebaseDatabase

class FacilityAdapter(
    private var facilities: List<Facility>,
    private val context: Context,
    private val onClick: (Facility) -> Unit
) : RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_facility, parent, false)
        return FacilityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val facility = facilities[position]
        holder.bind(facility, onClick)
    }

    override fun getItemCount(): Int = facilities.size

    fun updateFacilities(newFacilities: List<Facility>) {
        facilities = newFacilities
        notifyDataSetChanged()
    }

    inner class FacilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_facility)
        private val cabangTextView: TextView = itemView.findViewById(R.id.cabang_facility)
        private val bookingButton: Button = itemView.findViewById(R.id.button_booking)
        private val detailsButton: Button = itemView.findViewById(R.id.button_details)

        fun bind(facility: Facility, onClick: (Facility) -> Unit) {
            nameTextView.text = facility.nama_prasarana_olahraga
            cabangTextView.text = facility.cabang_olahraga

            bookingButton.setOnClickListener { showDatePickerDialog(facility) }

            detailsButton.setOnClickListener {
                val intent = Intent(context, FacilityDetailActivity::class.java)
                intent.putExtra("facility", facility)
                context.startActivity(intent)
            }
        }

        private fun showDatePickerDialog(facility: Facility) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_date_picker, null)
            val editTextName: EditText = dialogView.findViewById(R.id.edittext_name)
            val editTextContact: EditText = dialogView.findViewById(R.id.edittext_contact)
            val editTextBookingDate: EditText = dialogView.findViewById(R.id.edittext_booking_date)

            val dialog = AlertDialog.Builder(context)
                .setTitle("Booking Facility")
                .setView(dialogView)
                .setPositiveButton("Book") { _, _ ->
                    val name = editTextName.text.toString()
                    val contact = editTextContact.text.toString()
                    val bookingDate = editTextBookingDate.text.toString()

                    if (name.isNotEmpty() && contact.isNotEmpty() && bookingDate.isNotEmpty()) {
                        val bookingId = FirebaseDatabase.getInstance().getReference("bookings").push().key
                        val booking = Booking(
                            id = bookingId,
                            facilityId = facility.id,
                            name = name,
                            contact = contact,
                            date = bookingDate
                        )

                        bookingId?.let {
                            FirebaseDatabase.getInstance().getReference("bookings").child(it)
                                .setValue(booking).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Booking berhasil disimpan", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Booking gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            editTextBookingDate.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih Tanggal Booking")
                    .build()

                datePicker.show((context as AppCompatActivity).supportFragmentManager, "DATE_PICKER")

                datePicker.addOnPositiveButtonClickListener { selection ->
                    editTextBookingDate.setText(datePicker.headerText)
                }
            }

            dialog.show()
        }
    }
}
