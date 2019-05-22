package com.example.rssreader.screens

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.example.rssreader.MainActivity
import com.example.rssreader.R
import com.example.rssreader.adapters.LinkItemAdapter
import com.example.rssreader.models.LinkItem

import java.util.Objects


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.root_preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deleteEntry = findPreference<Preference>(getString(R.string.delete_entry))
        val addEntry = findPreference<Preference>(getString(R.string.add_entry))
        val defaults = findPreference<Preference>(getString(R.string.preferences_edittext_default))!!

        assert(deleteEntry != null)
        assert(addEntry != null)

        defaults.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Reset links to default links?")

            dialog.setPositiveButton("Ok") { dialog1, _ ->
                (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).deleteListFromPreferences()
                dialog1.dismiss()
            }

            dialog.setNegativeButton("cancel") { dialog1, _ -> dialog1.dismiss() }
            dialog.show()
            true
        }

        addEntry!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            //Make new Dialog
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Set target Title & link")
            val context = context
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL

            val titleLabel = TextView(context)
            titleLabel.text = getString(R.string.preferences_edittext_title)
            layout.addView(titleLabel)

            val titleBox = EditText(context)
            titleBox.hint = "Title"
            layout.addView(titleBox) // Notice this is an add method

            val linkBox = EditText(context)
            linkBox.hint = "Link"
            layout.addView(linkBox) // Another add method
            dialog.setView(layout) // Again this is a set method, not add

            dialog.setNegativeButton("cancel") { dialog1, _ -> dialog1.dismiss() }

            dialog.setPositiveButton("Ok") { dialog1, _ ->
                val link = linkBox.text.toString()
                if (Patterns.WEB_URL.matcher(link.toLowerCase()).matches()) {
                    MainActivity.urls!!.add(LinkItem(titleBox.text.toString(), link))
                    (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).saveListInPreferences()
                    dialog1.dismiss()
                } else {
                    Toast.makeText(context, "Link format invalid!", Toast.LENGTH_LONG).show()
                }
            }

            dialog.show()
            true
        }

        deleteEntry!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val builderSingle = AlertDialog.Builder(activity)
            builderSingle.setIcon(R.drawable.ic_launcher_foreground)
            builderSingle.setTitle("Select One Name:-")

            val arrayAdapter = MainActivity.urls?.let { it1 -> LinkItemAdapter(Objects.requireNonNull<FragmentActivity>(activity), R.id.postListView, it1) }

            builderSingle.setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }

            builderSingle.setAdapter(arrayAdapter) { _, which ->
                val strName = arrayAdapter?.getItem(which)
                val builderInner = AlertDialog.Builder(activity)
                builderInner.setMessage(strName?.Title ?: "error")
                builderInner.setTitle("Your Selected Item to delete is")
                builderInner.setPositiveButton("Ok") { dialog1, _ ->
                    MainActivity.urls!!.removeAt(which)
                    (Objects.requireNonNull<FragmentActivity>(activity) as MainActivity).saveListInPreferences()
                    arrayAdapter?.notifyDataSetChanged()
                    dialog1.dismiss()
                }
                builderInner.setNegativeButton("Cancel") { dialog1, _ -> dialog1.dismiss() }
                builderInner.show()
            }
            builderSingle.show()
            true
        }
    }


}
