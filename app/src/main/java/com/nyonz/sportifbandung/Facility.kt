import android.os.Parcel
import android.os.Parcelable

data class Facility(
    val id: String = "",
    val alamat: String = "",
    val cabang_olahraga: String = "",
    val nama_prasarana_olahraga: String = "",
    val imageResId: Int = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama_prasarana_olahraga)
        parcel.writeString(alamat)
        parcel.writeString(cabang_olahraga)
        parcel.writeInt(imageResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Facility> {
        override fun createFromParcel(parcel: Parcel): Facility {
            return Facility(parcel)
        }

        override fun newArray(size: Int): Array<Facility?> {
            return arrayOfNulls(size)
        }
    }
}
