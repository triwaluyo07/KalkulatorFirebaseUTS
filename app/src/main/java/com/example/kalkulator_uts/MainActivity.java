package com.example.kalkulator_uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String tag = "MainActivity";
    private List<Hitstory> numbers = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter adapter;

    private EditText field1, field2;
    private RadioButton btn_tambah, btn_kurang, btn_kali, btn_bagi;
    private Button btn_hitung;
    private TextView hasil;
    private RadioGroup operator;

    private RecyclerView.LayoutManager manager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        hasil = findViewById(R.id.hasil);
        operator = findViewById(R.id.btnGroup);
        btn_tambah = findViewById(R.id.btn_tambah);
        btn_kurang = findViewById(R.id.btn_kurang);
        btn_kali = findViewById(R.id.btn_kali);
        btn_bagi = findViewById(R.id.btn_bagi);
        btn_hitung = findViewById(R.id.btn_hitung);
        recyclerView = findViewById(R.id.recView);

        manager = new LinearLayoutManager(getApplicationContext());
        adapter = new Adapter(numbers);

        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        showRecyclerView();

        btn_hitung.setEnabled(false);
        operator.setEnabled(false);
        btn_tambah.setEnabled(false);
        btn_kurang.setEnabled(false);
        btn_kali.setEnabled(false);
        btn_bagi.setEnabled(false);

        field1.addTextChangedListener(executeWatcher);
        field2.addTextChangedListener(executeWatcher);

        btn_hitung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(btn_tambah.isChecked())
                {
                    tambah();
                }
                else if(btn_kurang.isChecked())
                {
                    kurang();
                }
                else if(btn_kali.isChecked())
                {
                    kali();
                }
                else if(btn_bagi.isChecked())
                {
                    bagi();
                }
                else
                {
                    Log.w(tag, "Salah");
                }
            }
        });


    }

    private TextWatcher executeWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            String input1_input = field1.getText().toString().trim();
            String input2_input = field2.getText().toString().trim();

            btn_hitung.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            operator.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            btn_tambah.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            btn_kurang.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            btn_kali.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
            btn_bagi.setEnabled(!input1_input.isEmpty() && !input2_input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };

    public void tambah()
    {
        double first = Double.parseDouble(field1.getText().toString());
        double second = Double.parseDouble(field2.getText().toString());
        double res = first+second;
        String textResult = "" + first + " + " + second + " = " + res;

        hasil.setText(String.valueOf(res));
        addRiwayat(textResult);
    }

    public void kurang()
    {
        double first = Double.parseDouble(field1.getText().toString());
        double second = Double.parseDouble(field2.getText().toString());
        double res = first-second;
        String textResult = "" + first + " - " + second + " = " + res;

        hasil.setText(String.valueOf(res));
        addRiwayat(textResult);
    }

    public void kali()
    {
        double first = Double.parseDouble(field1.getText().toString());
        double second = Double.parseDouble(field2.getText().toString());
        double res = first*second;
        String textResult = "" + first + " x " + second + " = " + res;

        hasil.setText(String.valueOf(res));
        addRiwayat(textResult);
    }

    public void bagi()
    {
        double first = Double.parseDouble(field1.getText().toString());
        double second = Double.parseDouble(field2.getText().toString());
        double res = first/second;
        String textResult = "" + first + " / " + second + " = " + res;

        hasil.setText(String.valueOf(res));
        addRiwayat(textResult);
    }

    private void addRiwayat(String text)
    {
        Map<String,String> riwayat = new HashMap<>();
        riwayat.put("hasilHitung",text);
        db.collection("KalkulatorApik")
                .add(riwayat);
    }

    public void showRecyclerView()
    {
        db.collection("KalkulatorApik")
                .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                        {
                            Hitstory num = new Hitstory();
                            num.setHistory(documentSnapshot.get("hasilHitung").toString());
                            numbers.add(num);
                        }
                    }
                    }
                });

    }
}

