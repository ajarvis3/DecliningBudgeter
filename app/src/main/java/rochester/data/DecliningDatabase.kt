package rochester.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Account::class, Purchase::class], version=1)
abstract class DecliningDatabase : RoomDatabase() {
    abstract fun AccountDao() : AccountDao
    abstract fun PurchaseDao() : PurchaseDao
}