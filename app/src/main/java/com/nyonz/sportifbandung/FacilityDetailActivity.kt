package com.nyonz.sportifbandung

import Facility
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class Booking(
    val id: String? = null,
    val facilityId: String = "",
    val name: String = "",
    val contact: String = "",
    val date: String = ""
)

class FacilityDetailActivity : AppCompatActivity() {

    private lateinit var imageFacilityDetail: ImageView
    private lateinit var nameFacilityText: TextView
    private lateinit var descriptionFacilityDetail: TextView
    private lateinit var buttonBooking: Button
    private lateinit var buttonBack: ImageButton
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facility_detail)

        // Inisialisasi view dari layout
        imageFacilityDetail = findViewById(R.id.image_facility_detail)
        nameFacilityText = findViewById(R.id.nama_prasarana_olahraga)
        descriptionFacilityDetail = findViewById(R.id.description_facility_detail)
        buttonBooking = findViewById(R.id.button_booking)
        buttonBack = findViewById(R.id.button_back)
        dbRef = FirebaseDatabase.getInstance().getReference("bookings")

        val facility = intent.getParcelableExtra<Facility>("facility")
        bind(facility)

        // Listener untuk tombol Booking
        buttonBooking.setOnClickListener {
            facility?.let { showDatePickerDialog(it) }
        }

        // Listener untuk tombol kembali
        buttonBack.setOnClickListener {
            finish() // Menutup activity saat tombol back ditekan
        }
    }

    private fun bind(facility: Facility?) {
        facility?.let {
            nameFacilityText.text = it.nama_prasarana_olahraga
            descriptionFacilityDetail.text = it.alamat

            Glide.with(this)
                .load(it.imageResId.takeIf { id -> id != 0 } ?: R.drawable.ic_facility_placeholder)
                .into(imageFacilityDetail)
        }
    }

    private fun showDatePickerDialog(facility: Facility) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker, null)
        val editTextName: EditText = dialogView.findViewById(R.id.edittext_name)
        val editTextContact: EditText = dialogView.findViewById(R.id.edittext_contact)
        val editTextBookingDate: EditText = dialogView.findViewById(R.id.edittext_booking_date)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Booking Facility")
            .setView(dialogView)
            .setPositiveButton("Book") { _, _ ->
                val name = editTextName.text.toString()
                val contact = editTextContact.text.toString()
                val bookingDate = editTextBookingDate.text.toString()

                if (name.isNotEmpty() && contact.isNotEmpty() && bookingDate.isNotEmpty()) {
                    val bookingId = dbRef.push().key
                    val booking = Booking(
                        id = bookingId,
                        facilityId = facility.id,
                        name = name,
                        contact = contact,
                        date = bookingDate
                    )

                    bookingId?.let {
                        dbRef.child(it)
                            .setValue(booking)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Booking berhasil disimpan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Booking gagal: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
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

            datePicker.show(supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { selection ->
                editTextBookingDate.setText(datePicker.headerText)
            }
        }

        dialog.show()
    }
}
