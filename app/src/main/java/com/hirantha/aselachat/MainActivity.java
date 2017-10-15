package com.hirantha.aselachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    public static MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //add fragment to the container
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new LoginFragment()).commit();
    }

    /**
     * Fragments
     *
     * activity ekakin thawa activity ekak open karala back karoth bn apahu kalin activity ekatama yanawa.
     * login wage thenakadi log unata passe back karoth aye login ekatama enawnane.eka awlk.ewage thenakata use kaaranna thama fragment thyenne
     * ekedi eka activtiy ekai fragment godai
     * activity eke container ekak dala eka udata ape fragment replace karan eka karanne;
     *
     */
}
