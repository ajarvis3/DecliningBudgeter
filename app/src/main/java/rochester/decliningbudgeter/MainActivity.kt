package rochester.decliningbudgeter

import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_purchase.view.*
import kotlinx.android.synthetic.main.fragment_add_acount_dialog.view.*
import rochester.data.Account
import rochester.data.DecliningDatabase
import rochester.data.Purchase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : FragmentActivity(), AdapterView.OnItemSelectedListener {
    // The Db used
    private var mDb : DecliningDatabase? = null
    var currAccount : Account? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO make db async
        mDb = Room.databaseBuilder(applicationContext, DecliningDatabase::class.java, "Declining").allowMainThreadQueries().build()
        setAccounts()
        Account_List.onItemSelectedListener = this

    }

    /**
     * Sets the accounts that exist in the spinner
     */
    private fun setAccounts() {
        val accounts : List<Account> = mDb?.AccountDao()!!.getAllAccounts()
        val accStrings = accounts.map{it.name}
        Account_List.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accStrings)
    }

    /**
     * Onclick listener
     * Gets info from user to build an account
     * Updates the accounts (hopefully?)
     */
    fun addAccount(v : View) {
        var getInfo : AlertDialog? = let {
            val builder = AlertDialog.Builder(it)
            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.fragment_add_acount_dialog, null)
            builder.setView(view)

            // On click listeners
//            view.s_d_button.setOnClickListener {
//                val datePicker = MyDatePickerDialog()
//                datePicker.setTView(view.start_date)
//            }
//            view.e_d_button.setOnClickListener {
//                val datePicker = MyDatePickerDialog()
//                datePicker.setTView(view.end_date)
//            }


            builder.setNeutralButton(R.string.finished) { _, _ ->
                val name = view.account_name.text.toString()
                val owner = view.account_owner.text.toString()
                val balance = view.account_balance.text.toString().toDouble()
                val startDate = view.start_date.text.toString()
                val endDate = view.end_date.text.toString()

                mDb?.AccountDao()?.insertAccount(Account(name, owner, balance, startDate, endDate))
                setAccounts()
            }
            builder.create()
        }

        getInfo!!.show()

    }

    /**
     * Adds a purchase
     */
    fun addPurchase(v : View) {
        var getPurch : AlertDialog? = let {
            val builder = AlertDialog.Builder(it)
            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.add_purchase, null)
            builder.setView(view)
            builder.setNeutralButton(R.string.finished) {_,_ ->
                val num = view.purchase_amount.text.toString().toDouble()
                val date = Calendar.MONTH.toString() + "/" + Calendar.DAY_OF_MONTH + "/" + Calendar.YEAR

                // Apparently it's called an Elvis Operator. I think it's noteworthy
                val accName = currAccount?.name ?: ""

                mDb!!.PurchaseDao().insertPurchases(Purchase(UUID.randomUUID().toString(), num, date, accName))
                if (currAccount != null)
                    setPurchasesStuff(currAccount!!)
            }

            builder.create()
        }

        getPurch!!.show()
    }

    /**
     * Updates lists and stats.
     * Tired so can't be more specific right now.
     */
    fun setPurchasesStuff(account : Account) {
        val purchases : List<Purchase> = mDb!!.PurchaseDao().getPurchases(account.name)

        // Calculations
        val purStrings = purchases.map{it.amount}
        val purchaseValue : Double = purchases.fold(0.0) { sum, e -> sum + e.amount}
        val moneyLeft = account.balance - purchaseValue
        val today = Date()
        val numberFormat = NumberFormat.getCurrencyInstance()
        val firstDay = SimpleDateFormat("MM-dd-yyyy").parse(account.first)
        val lastDay = SimpleDateFormat("MM-dd-yyyy").parse(account.last)
        val numToToday = TimeUnit.DAYS.convert(today.time - firstDay.time, TimeUnit.MILLISECONDS).toInt()
        val daysLeft = TimeUnit.DAYS.convert(lastDay.time - today.time, TimeUnit.MILLISECONDS).toInt()

        // Things calculated that I care about
        val avgSpent = if (numToToday != 0) purchaseValue/numToToday else purchaseValue
        val avgSpend = if (daysLeft != 0) moneyLeft/daysLeft else moneyLeft

        Purchase_List.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, purStrings)

        val uMoneyLeft = numberFormat.format(moneyLeft)
        val uAvgSpent = numberFormat.format(avgSpent)
        val uAvgSpend = numberFormat.format(avgSpend)
        BalanceTV.text = "Balance: $uMoneyLeft"
        AverageSpentTV.text = "Average Spent: $uAvgSpent"
        AverageSpendTV.text = "Average Spend: $uAvgSpend"
    }

    /**
     * Deals with selection of an account
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Get stuff already set - Account and purchases
        val item : String = parent!!.getItemAtPosition(position).toString()
        val account : Account = mDb!!.AccountDao().getAccount(item)

        currAccount = account
        setPurchasesStuff(account)
    }

    /**
     * Nothin'
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Does nothing.
    }

}
