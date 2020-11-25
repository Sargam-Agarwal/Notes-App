package com.sargam.college.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNoteDetails extends AppCompatActivity {

    EditText mTitle;
    EditText mDescription;
    EditText mStartDate;
    EditText mLastDate;
    EditText mPriority;
    Button mAddNote;
    FirebaseFirestore db;
    DocumentReference mDocRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_details);

        Log.d("Problem", "onCreate: created");

        db = FirebaseFirestore.getInstance();
        initializeFields();

        Intent intent = getIntent();

        if(intent.getStringExtra("Document Name").equals( "")){

            mDocRef = db.collection("notes").document();
            mAddNote.setText("ADD");

        }else{

            mAddNote.setText("UPDATE");
            mDocRef = db.collection("notes").document(Objects.requireNonNull(intent.getStringExtra("Document Name")));
            mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {

                   Log.d("Problem", "onSuccess: Success");

                   if(documentSnapshot.exists()){
                       Map<String,Object> mDocValue = documentSnapshot.getData();
                       mTitle.setText(mDocValue.get("title").toString());
                       mDescription.setText(mDocValue.get("description").toString());
                       mStartDate.setText(mDocValue.get("startDate").toString());
                       mLastDate.setText(mDocValue.get("lastDate").toString());
                       mPriority.setText(mDocValue.get("priority").toString());
                   }
                   else{
                       Toast.makeText(AddNoteDetails.this, "Note Doesn't Exist!", Toast.LENGTH_SHORT).show();
                   }

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {

                   Log.d("Problem", "onFailure: "+e);
               }
           });

            Log.d("Problem", "onClick: itemView Clicked"+intent.getStringExtra("Document Name"));



        }

        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

    }

    private void saveNote() {

        String title = mTitle.getText().toString().trim();
        String description = mDescription.getText().toString().trim();
        String startDate = mStartDate.getText().toString().trim();
        String lastDate = mLastDate.getText().toString().trim();
        String priority = mPriority.getText().toString().trim();

        Map<String,String> noteDetails = new HashMap<>();

        if(TextUtils.isEmpty(title)){
            mTitle.setError("Required !");
            return;

        }
        else{
            noteDetails.put("title",title);
        }

        if(!TextUtils.isEmpty(description)){
            noteDetails.put("description",description);
        }else{
            noteDetails.put("description","");
        }

        if(!TextUtils.isEmpty(startDate)){
         noteDetails.put("startDate",startDate);
        }else{
            noteDetails.put("startDate","");
        }


        if(!TextUtils.isEmpty(lastDate)){

            noteDetails.put("lastDate",lastDate);
        }else{
            noteDetails.put("lastDate","");
        }


        if(TextUtils.isEmpty(priority)){
            mPriority.setError("Required !");
            return;
        }
        else{
            noteDetails.put("priority",priority);
        }

        mDocRef.set(noteDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddNoteDetails.this, "Note Updated Successfully !", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddNoteDetails.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void initializeFields() {


        mTitle = findViewById(R.id.title_et);
        mDescription = findViewById(R.id.description_et);
        mStartDate = findViewById(R.id.start_date_et);
        mLastDate = findViewById(R.id.last_date_et);
        mPriority = findViewById(R.id.priority_et);
        mAddNote = findViewById(R.id.add_note_button);


    }
}
