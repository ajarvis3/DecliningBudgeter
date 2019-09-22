package rochester.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["id"])
data class Purchase(
    @ColumnInfo(name="id") val id : String,
    @ColumnInfo(name="amount") val amount: Double,
    @ColumnInfo(name="dateTime") val date: String,
    @ColumnInfo(name="account_name") val account: String
)