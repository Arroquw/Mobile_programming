package com.example.rssreader.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.rssreader.MainActivity;
import com.example.rssreader.R;
import com.example.rssreader.models.LinkItem;

import java.util.ArrayList;
import java.util.Objects;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.root_preferences);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference deleteEntry = findPreference(getString(R.string.delete_entry));
        Preference addEntry = findPreference(getString(R.string.add_entry));
        Preference defaults = findPreference(getString(R.string.preferences_edittext_default));

        assert defaults != null;
        assert deleteEntry != null;
        assert addEntry != null;

        defaults.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Reset links to default links?");

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) Objects.requireNonNull(getActivity())).deleteListFromPreferences();
                        ((MainActivity) getActivity()).setListToDefaults();
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        addEntry.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Make new Dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Set target Title & link");
                final Context context = getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView titleLabel = new TextView(context);
                titleLabel.setText(getString(R.string.preferences_edittext_title));
                layout.addView(titleLabel);

                final EditText titleBox = new EditText(context);
                titleBox.setHint("Title");
                layout.addView(titleBox);

                final EditText linkBox = new EditText(context);
                linkBox.setHint("Link");
                layout.addView(linkBox);
                dialog.setView(layout);

                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String link = linkBox.getText().toString();
                        if (Patterns.WEB_URL.matcher(link.toLowerCase()).matches()) {
                            MainActivity.urls.add(new LinkItem(titleBox.getText().toString(), link));
                            ((MainActivity) Objects.requireNonNull(getActivity())).saveListInPreferences();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Link format invalid!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
                return true;
            }
        });

        deleteEntry.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setIcon(R.drawable.ic_menu_gallery);
                builderSingle.setTitle("Select One Name:-");

                ArrayList<String> newArray = new ArrayList<>();
                for (LinkItem i :
                     MainActivity.urls) {
                    newArray.add(i.Title);
                }

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.settings_list_item, newArray);

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int index = which;
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                        builderInner.setMessage(strName != null ? strName : "error");
                        builderInner.setTitle("Your Selected Item to delete is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.urls.remove(index);
                                ((MainActivity) Objects.requireNonNull(getActivity())).saveListInPreferences();
                                arrayAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        builderInner.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
                return true;
            }
        });
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }


}
