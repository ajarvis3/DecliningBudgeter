package rochester.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["account_name"])
data class Account (
    @ColumnInfo(name="account_name") val name : String,
    @ColumnInfo(name="owner") val owner : String,
    @ColumnInfo(name="balance") val balance : Double,
    @ColumnInfo(name="first_day") val first : String,
    @ColumnInfo(name="last_day") val last : String
)