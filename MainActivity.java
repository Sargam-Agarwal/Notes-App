package com.sargam.college.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mAddBtn;
    RecyclerView mRecyclerView;

    FirestoreRecyclerAdapter<NoteValues,NoteViewHolder> adapter;
    FirebaseFirestore db;
    CollectionReference mCollectionRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        initializeViews();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirestoreRecyclerOptions<NoteValues> options ;


        mCollectionRef = db.collection("notes");

        Query query = mCollectionRef.orderBy("priority", Query.Direction.ASCENDING) ;
        options = new FirestoreRecyclerOptions.Builder<NoteValues>().setQuery(query,NoteValues.class).build();

        adapter = new FirestoreRecyclerAdapter<NoteValues, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteValues model) {

                holder.mTitle.setText(model.getTitle());
                holder.mDescription.setText(model.getDescription());
                holder.mPriority.setText(model.getPriority());
                holder.mStartDate.setText(model.getStartDate());
                holder.mLastDate.setText(model.getLastDate());

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.single_note,parent,false);

                return new NoteViewHolder(view);
            }


        };

        mRecyclerView.setAdapter(adapter);




        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNoteDetails.class);
                intent.putExtra("Document Name","");
                startActivity(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());


            }

        }).attachToRecyclerView(mRecyclerView);
    }





    private void deleteItem(int position) {

        adapter.getSnapshots().getSnapshot(position).getReference().delete();

    }




    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mDescription;
        TextView mStartDate;
        TextView mLastDate;
        TextView mPriority;


        NoteViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title_cardView_tv);
            mDescription = itemView.findViewById(R.id.description_cardView_tv);
            mStartDate = itemView.findViewById(R.id.startDate_cardView_tv);
            mLastDate = itemView.findViewById(R.id.lastDate_cardView_tv);
            mPriority = itemView.findViewById(R.id.priority_cardView_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this,AddNoteDetails.class);
                    intent.putExtra("Document Name",adapter.getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    startActivity(intent);

                }
            });

        }


    }

    private void initializeViews() {
        mAddBtn = findViewById(R.id.add_button);
        mRecyclerView = findViewById(R.id.recycler_view);
    }
}
