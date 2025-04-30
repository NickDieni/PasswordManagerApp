package com.example.myfirstapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewAccounts;
    private Button btnAdd;
    private EditText edtName, edtPassword;
    private ArrayList<Account> accounts = new ArrayList<>();
    private AccountAdapter accountAdapter;
    private TextView txtName, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views initialization
        listViewAccounts = findViewById(R.id.list_view);
        btnAdd = findViewById(R.id.btn_use);
        edtName = findViewById(R.id.edt_name);
        edtPassword = findViewById(R.id.edt_password);
        txtName = findViewById(R.id.txt_name);
        txtPassword = findViewById(R.id.txt_password);

        // Setup the adapter
        accountAdapter = new AccountAdapter(this, accounts);
        listViewAccounts.setAdapter(accountAdapter);

        // Add Account button
        btnAdd.setOnClickListener(v -> {
            saveAccount();
        });

        // Show details and long press listener
        listViewAccounts.setOnItemClickListener((parent, view, position, id) -> showAccountDetails(position));
    }

    private void saveAccount() {
        String accountName = edtName.getText().toString();
        String password = edtPassword.getText().toString();

        if (accountName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Account newAccount = new Account(accountName, "", password);
        accounts.add(newAccount);
        accountAdapter.notifyDataSetChanged();
        clearInputFields();
        Toast.makeText(this, "Account added", Toast.LENGTH_SHORT).show();
    }

    private void showAccountDetails(int position) {
        Account selectedAccount = accounts.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(selectedAccount.getAccountName())
                .setMessage("Password: " + selectedAccount.getPassword())
                .setPositiveButton("OK", null)
                .show();
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Delete", (dialog, which) -> deleteAccount(position))
                .setNegativeButton("Cancel", null)
                .show();
    }



    private void deleteAccount(int position) {
        accounts.remove(position);
        accountAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
    }

    private void clearInputFields() {
        edtName.getText().clear();
        edtPassword.getText().clear();
    }

    // Custom adapter
    private class AccountAdapter extends ArrayAdapter<Account> {
        public AccountAdapter(Context context, ArrayList<Account> accounts) {
            super(context, 0, accounts);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Account account = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);
            }

            TextView tvAccountName = convertView.findViewById(R.id.text_account_name);
            Button btnDelete = convertView.findViewById(R.id.btn_delete);
            Button btnShowPassword = convertView.findViewById(R.id.btn_show_password);

            if (account != null) {
                tvAccountName.setText(account.getAccountName());

                // Delete button logic
                btnDelete.setOnClickListener(v -> {
                    showDeleteConfirmationDialog(position);
                });

                // Show Password button logic
                btnShowPassword.setOnClickListener(v -> {
                    showPassword(account.getPassword());
                });
            }

            return convertView;
        }
    }
    private void showPassword(String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password")
                .setMessage(password)
                .setPositiveButton("OK", null)
                .show();


    }
}