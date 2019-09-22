package rochester.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.sql.Date

@Dao
interface PurchaseDao {
    @Query ("SELECT * FROM purchase")
    fun getAll(): List<Purchase>

    @Query ("SELECT * FROM purchase WHERE account_name LIKE :account")
    fun getPurchases(account: String): List<Purchase>

    @Query ("SELECT * FROM purchase WHERE dateTime LIKE :date AND account_name LIKE :account")
    fun getPurchases(date: String, account: String): List<Purchase>

    @Insert
    fun insertPurchases(vararg purchases : Purchase)

    @Delete
    fun deletePurchase(purchase: Purchase)
}