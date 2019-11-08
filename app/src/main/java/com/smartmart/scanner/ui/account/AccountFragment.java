package com.smartmart.scanner.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.smartmart.scanner.R;

import static android.support.constraint.Constraints.TAG;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private EditText accountName;
    private EditText accountEmail;
    private View root;
    private MenuItem menuItem;
    private MenuItem newmenuItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        root = inflater.inflate(R.layout.fragment_account, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        accountName = root.findViewById(R.id.AccountName);
        accountEmail = root.findViewById(R.id.AccountEmail);
        accountName.setEnabled(false);
        accountEmail.setEnabled(false);
        accountViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String message = "You click fragment ";

        if(itemId == R.id.edit)
        {
//            message += "Search menu";

            accountName.setEnabled(true);
            accountEmail.setEnabled(true);
            menuItem.setVisible(false);
            newmenuItem.setVisible(true);

        }
        else if(itemId == R.id.save)
        {
//            message += "Search menu";

            accountName.setEnabled(false);
            accountEmail.setEnabled(false);
            newmenuItem.setVisible(false);
            menuItem.setVisible(true);

        }

/*        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(message);
        alertDialog.show();*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItem = menu.findItem(R.id.edit);
        newmenuItem = menu.findItem(R.id.save);
        newmenuItem.setVisible(false);
    }

}