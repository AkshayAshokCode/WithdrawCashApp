package com.akshayashokcode.withdrawcashapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class WithdrawRequest(
    val userId: String,
    val transactionId: String,
    val requestType: String,
    val amount: Double,
    val accountNumber: String,
    val currency: String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(transactionId)
        parcel.writeString(requestType)
        parcel.writeDouble(amount)
        parcel.writeString(accountNumber)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WithdrawRequest> {
        override fun createFromParcel(parcel: Parcel): WithdrawRequest {
            return WithdrawRequest(parcel)
        }

        override fun newArray(size: Int): Array<WithdrawRequest?> {
            return arrayOfNulls(size)
        }
    }

}
