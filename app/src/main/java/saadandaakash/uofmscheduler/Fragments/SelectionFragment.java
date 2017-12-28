package saadandaakash.uofmscheduler.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import saadandaakash.uofmscheduler.Adapters.AutocompleteAdapter;
import saadandaakash.uofmscheduler.ClearableAutoCompleteTextView;
import saadandaakash.uofmscheduler.R;
import saadandaakash.uofmscheduler.Utility;

/**
 * Created by Aakash on 12/22/2017.
 * Aakash, this is your fragment
 * Follow the directions below on how to change between fragments
 */

public class SelectionFragment extends Fragment {

    private String termCode = "2170";
    private String schoolCode = null;
    private String subjectCode = null;
    private String catalogNum = null;

    public Button submitButton;
    public ClearableAutoCompleteTextView editSubject;

    private static Map<String, String> schoolData;
    private static TreeMap<String, String> subjectData = new TreeMap<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Inflate the layout into the specified container
        //True if you can inflate the layout and then attach it directly to the root of the
        //container
        //False if you want to inflate the layout and then return that View
        return inflater.inflate(R.layout.selection_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        try{
            submitButton = (Button)getView().findViewById(R.id.submitCourseInfo);
            editSubject = (ClearableAutoCompleteTextView) getView().findViewById(R.id.enterSubject);

            // set font for button and input fields
            Typeface t = Typeface.createFromAsset(getActivity().getAssets(),
                    "fonts/Quicksand-Regular.otf");
            submitButton.setTypeface(t);
            editSubject.setTypeface(t);

            // create map of schools and their codes
            schoolData = Utility.readMapFromFile(
                    getActivity(),
                    "schools.json",
                    "SchoolCode",
                    "SchoolDescr");
            final Set<String> SCHOOLS = schoolData.keySet();

            // go through each school and get the subjects 
            for (String schoolCode : SCHOOLS) {
                // read subjects from file
                Map<String, String> subject = Utility.readMapFromFile(
                        getActivity(),
                        schoolCode + ".json",
                        "SubjectCode",
                        "SubjectDescr"
                );
                //System.out.println(subject);
                for (Map.Entry<String, String> entry : subject.entrySet()) {
                    subjectData.putAll(subject);
                    //System.out.println(entry.getValue());
                }
            }
            ArrayList<String> subjects = new ArrayList<>();
            for (Map.Entry<String, String> entry : subjectData.entrySet()) {
                subjects.add(entry.getKey() + " - " + entry.getValue());
            }

            // create array adapter for subject field, currently empty
            // will be populated once the school is entered
            final AutocompleteAdapter subject_adapter = new AutocompleteAdapter(getActivity(), subjects);
            editSubject.setAdapter(subject_adapter);
            editSubject.setThreshold(1);


            // submit button clicked
            submitButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {

                            try {
                                // get subject field
                                subjectCode = editSubject.getText().toString();

                                // check to make sure field is not empty and in right format
                                if (!subjectCode.trim().isEmpty()) {

                                    // get the actual code from the whole string
                                    if (subjectCode.contains(" ")) {
                                        subjectCode = subjectCode.substring(0, subjectCode.indexOf(" "));
                                    }

                                    // convert whatever was entered to uppercase
                                    subjectCode = subjectCode.toUpperCase();

                                    System.out.println("SUBJECT: " + subjectCode);

                                    // check to make sure subject code is valid by looking for value in map
                                    if (subjectData.get(subjectCode) != null) {

                                        CoursesFragment fragment = CoursesFragment.newInstance(subjectCode);
                                        Utility.hideKeyboard(getActivity());

                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.container, fragment)
                                                .addToBackStack("SELECTION FRAGMENT")
                                                .commit();
                                    }
                                }
                            }
                            catch (NullPointerException n) {
                                n.printStackTrace();
                            }
                        }
                    }
            );

            // if there is text in the field, show the clear button
            // if it is empty, hide the clear button
            editSubject.hideClearButton();
            editSubject.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String text = editSubject.getText().toString();
                    if (text.length() == 0) {
                        editSubject.hideClearButton();
                    }
                    else {
                        editSubject.showClearButton();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
