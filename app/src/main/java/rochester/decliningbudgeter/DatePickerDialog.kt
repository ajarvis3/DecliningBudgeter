package rochester.decliningbudgeter

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.DatePicker
import android.widget.TextView

/**
 * Taken from https://developer.android.com/guide/topics/ui/controls/pickers.html#DatePicker
 */
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var mSetView : TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }


    /**
     * Stores the date in the member text view
     */
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        mSetView?.text = "$month/$day/$year"
    }

    /**
     * Sets member mSetView to v
     */
    fun setTView(v : TextView) {
        mSetView = v
    }
}
