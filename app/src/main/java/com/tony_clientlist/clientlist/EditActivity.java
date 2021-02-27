package com.tony_clientlist.clientlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tony_clientlist.clientlist.database.AppDataBase;
import com.tony_clientlist.clientlist.database.AppExecuter;
import com.tony_clientlist.clientlist.database.Client;
import com.tony_clientlist.clientlist.utils.Constans;


public class EditActivity extends AppCompatActivity {
    private EditText edName, edSecName, edTel, edDisc;
    private CheckBox chImp1, chImp2, chImp3, chSpecial;
    private AppDataBase myDb;
    private CheckBox[] chBArray = new CheckBox[3];
    private int importance = 4;
    private int special = 0;
    private FloatingActionButton fab;
    private boolean isEdit = false;
    private boolean new_user = false;
    private int id;
    private ActionBar actionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        this.setTitle(R.string.edit_activiy);
        init();
        getMyIntent();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIportanceFromCh();
                if (!TextUtils.isEmpty(edName.getText().toString()) && !TextUtils.isEmpty(edSecName.getText().toString())
                        && !TextUtils.isEmpty(edTel.getText().toString())&& importance!=4) {
                    AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (isEdit) {

                                Client client = new Client(edName.getText().toString(),
                                        edSecName.getText().toString(), edTel.getText().toString(),
                                        importance, edDisc.getText().toString(), special);
                                client.setId(id);
                                myDb.clientDAO().updateClient(client);
                                finish();

                            } else {
                                Client client = new Client(edName.getText().toString(), edSecName.getText().toString(), edTel.getText().toString(), importance, edDisc.getText().toString(), special);
                                myDb.clientDAO().insertClient(client);
                                finish();
                            }


                        }
                    });
                }
                else {
                    Toast.makeText(EditActivity.this, R.string.warning_if_empty, Toast.LENGTH_LONG).show();
                }

            }
        });
        ;



    }

    private void init() {
        if(getSupportActionBar()!=null){
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        fab = findViewById(R.id.fb);
        myDb = AppDataBase.getInstance(getApplicationContext());
        edName = findViewById(R.id.edName);
        edSecName = findViewById(R.id.edSecName);
        edTel = findViewById(R.id.edTel);
        edDisc = findViewById(R.id.edDisc);


        chImp1 = findViewById(R.id.checkBoxImp1);
        chImp2 = findViewById(R.id.checkBoxImp2);
        chImp3 = findViewById(R.id.checkBoxImp3);
        chBArray[0] = chImp1;
        chBArray[1] = chImp2;
        chBArray[2] = chImp3;
        chSpecial = findViewById(R.id.checkBoxSpecial);


    }


    public void onClickCh1(View view) {
        chImp2.setChecked(false);
        chImp3.setChecked(false);
    }

    private void getMyIntent() {
        Intent i = getIntent();
        if (i != null) {
            if (i.getStringExtra(Constans.NAME_KEY) != null) {

                setIsEdit(false);
                edName.setText(i.getStringExtra(Constans.NAME_KEY));
                edSecName.setText(i.getStringExtra(Constans.SEC_NAME_KEY));
                edTel.setText(i.getStringExtra(Constans.TEL_KEY));
                edDisc.setText(i.getStringExtra(Constans.DESC_KEY));
                chBArray[i.getIntExtra(Constans.IMP_KEY, 0)].setChecked(true);
                if (i.getIntExtra(Constans.SP_KEY, 0) == 1) chSpecial.setChecked(true);
                new_user = false;
                id = i.getIntExtra(Constans.ID_KEY, 0);

            } else {
                new_user = true;
            }


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!new_user) getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_edit) {
            setIsEdit(true);
        }
        else if(id == R.id.id_delete)
        {
            deleteDialog();
        }
        else if (id==R.id.id_call){
            EditText number=(EditText)findViewById(R.id.edTel);
            String toDial="tel:"+number.getText().toString();
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toDial)));


        }
        else if(item.getItemId()== android.R.id.home){
            finish();

        }

        return true;
    }

    public void onClickCh2(View view) {
        chImp1.setChecked(false);
        chImp3.setChecked(false);
    }

    public void onClickCh3(View view) {
        chImp1.setChecked(false);
        chImp2.setChecked(false);
    }
    private void deleteDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_message);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {

                            Client client = new Client(edName.getText().toString(),
                                    edSecName.getText().toString(), edTel.getText().toString(),
                                    importance, edDisc.getText().toString(), special);
                            client.setId(id);
                            myDb.clientDAO().deleteClient(client);
                            finish();




                    }
                });
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void setIsEdit(boolean isEdit) {
        if (isEdit) {
            fab.show();
        } else {
            fab.hide();
        }
        this.isEdit = isEdit;
        chImp1.setClickable(isEdit);
        chImp2.setClickable(isEdit);
        chImp3.setClickable(isEdit);
        chSpecial.setClickable(isEdit);
        edName.setClickable(isEdit);
        edSecName.setClickable(isEdit);
        edTel.setClickable(isEdit);
        edDisc.setClickable(isEdit);
        edName.setFocusable(isEdit);
        edSecName.setFocusable(isEdit);
        edTel.setFocusable(isEdit);
        edDisc.setFocusable(isEdit);
        edName.setFocusableInTouchMode(isEdit);
        edSecName.setFocusableInTouchMode(isEdit);
        edTel.setFocusableInTouchMode(isEdit);
        edDisc.setFocusableInTouchMode(isEdit);
    }

    private void getIportanceFromCh() {
        if (chImp1.isChecked()) {
            importance = 0;
        } else if (chImp2.isChecked()) {

            importance = 1;
        } else if (chImp3.isChecked()) {

            importance = 2;
        }

        if (chSpecial.isChecked()) special = 1;
    }


}
