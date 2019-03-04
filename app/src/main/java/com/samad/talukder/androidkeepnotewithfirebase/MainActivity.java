package com.samad.talukder.androidkeepnotewithfirebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samad.talukder.androidkeepnotewithfirebase.account.LogInActivity;
import com.samad.talukder.androidkeepnotewithfirebase.adapter.ViewNoteAdapter;
import com.samad.talukder.androidkeepnotewithfirebase.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private TextView currentUserTV;
    private FloatingActionButton fabBtn;
    private RecyclerView viewNote;
    private List<Note> noteArrayList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        // Get current user
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uID = currentUser.getUid();
        //
        dbReference = FirebaseDatabase.getInstance().getReference().child("KeepNote").child(uID);
        dbReference.keepSynced(true);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser == null) {
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                    finish();
                }
            }
        };
        //currentUserTV = findViewById(R.id.currentUserTV);
        fabBtn = findViewById(R.id.fabBtn);
        viewNote = findViewById(R.id.viewNoteRV);
        //currentUserTV.setText("Welcome, " + currentUser.getEmail());

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.include_add_note, null);
                dialogBuilder.setView(view);
                final AlertDialog dialog = dialogBuilder.create();
                final EditText noteTitle = view.findViewById(R.id.noteTitleET);
                final EditText noteText = view.findViewById(R.id.noteTextET);
                Button clearBtn = view.findViewById(R.id.button_clear);
                Button saveBtn = view.findViewById(R.id.button_save);
                clearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        noteText.setText("");
                    }
                });
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = dbReference.push().getKey();
                        String nTitle = noteTitle.getText().toString().trim();
                        String nText = noteText.getText().toString().trim();
                        //String currentDate = DateFormat.getDateInstance().format(new Date());
                        //String currentDate = String.valueOf(System.currentTimeMillis());
                        Date curDate = new Date();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy hh:mm:ss a");
                        String DateToStr = format.format(curDate);

                        if (nTitle.isEmpty()) {
                            nTitle = "Untitled";
                        }
                        noteTitle.setText(nTitle);
                        Note note = new Note(id, nTitle, nText, DateToStr);
                        dbReference.child(id).setValue(note);
                        Toast.makeText(MainActivity.this, "Note Add Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID) {
            case R.id.action_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    noteArrayList.add(note);
                }
                viewNote.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                //viewNote.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                viewNote.setHasFixedSize(true);
                viewNote.setAdapter(new ViewNoteAdapter(getApplicationContext(), noteArrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Data Not Loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
