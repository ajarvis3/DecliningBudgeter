package rochester.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao{
    @Query("SELECT * FROM account")
    fun getAllAccounts() : List<Account>

    @Query("SELECT * FROM account WHERE account_name LIKE :name")
    fun getAccount(name : String) : Account

    @Insert
    fun insertAccount(vararg accounts : Account)

    @Delete
    fun deleteAccount(account : Account)
}