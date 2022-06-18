package com.rajesh.gallary.ui.Dialogs;

import static com.rajesh.gallary.common.Constant.ANSWER;
import static com.rajesh.gallary.common.Constant.QUESTION;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rajesh.gallary.R;
import com.rajesh.gallary.utils.SavedData;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SecurityQuestionDialog extends DialogFragment {

    @Inject
    SavedData savedData;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.password_question_dialog, null);
        //get Array of Questions
        String[] Questions = getContext().getResources().getStringArray(R.array.securityQuestions);
        //Define Views
        AutoCompleteTextView QuestionView = v.findViewById(R.id.Security_Question);
        TextInputLayout QuestionLayout = v.findViewById(R.id.Security_Question_layout);
        TextInputEditText AnswerView = v.findViewById(R.id.QuestionAnswer_Edx);
        TextInputLayout AnswerLayout = v.findViewById(R.id.QuestionAnswer_layout);
        Button Next_Btn = v.findViewById(R.id.Question_ok_Btn);
        Button cancel_Btn = v.findViewById(R.id.Question_cancle_Btn);
        //Initialize Question adapter
        ArrayAdapter<String> questionAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.security_item, Questions);
        //Apply adapter to Auto complete text view
        QuestionView.setAdapter(questionAdapter);
        //Handle on buttons clicks
        Next_Btn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();
            if (AnswerView.getText().length() > 0 && QuestionView.getText().length() > 0) {
                //save data into shared preferance
                savedData.setSecurity(QUESTION, QuestionView.getText().toString());
                savedData.setSecurity(ANSWER, AnswerView.getText().toString());
                // close dialog
                this.dismiss();
            } else {
                //set errors flags
                if (AnswerView.getText().length() == 0)
                    AnswerLayout.setError("Please Enter The Answer");
                if (QuestionView.getText().length() == 0)
                    QuestionLayout.setError("Please Choose Question");
            }
        });

        cancel_Btn.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Close", Toast.LENGTH_SHORT).show();
            this.dismiss();
        });

        //Set view to builder
        builder.setView(v);

        return builder.create();
    }


}
