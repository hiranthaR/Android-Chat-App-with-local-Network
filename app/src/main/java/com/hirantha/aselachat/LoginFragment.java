package com.hirantha.aselachat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText txtUserName,txtIP,txtPort;
    Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        txtIP = (EditText) rootView.findViewById(R.id.txtIP);
        txtPort = (EditText) rootView.findViewById(R.id.txtPort);
        txtUserName = (EditText) rootView.findViewById(R.id.txtUserName);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        //filter to accept only ips
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        txtIP.setFilters(filters);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ip = txtIP.getText().toString();
                final int port;
                if(txtPort.getText().toString().equals("")||txtPort.getText().toString()==null) {
                    port =1500;
                }else {
                    port = Integer.parseInt(txtPort.getText().toString());
                }
                final String username = txtUserName.getText().toString();

                if (ip==null||ip.equals("")||username.equals("")){
                    Toast.makeText(getContext(),"Fill all details",Toast.LENGTH_SHORT).show();
                }
                new Thread(){
                    @Override
                    public void run() {
                        new ServerConnector(ip,port,username).start();
                    }
                }.start();
            }
        });
        return rootView;
    }

}
