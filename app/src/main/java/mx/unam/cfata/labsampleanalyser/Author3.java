package mx.unam.cfata.labsampleanalyser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Author3 extends Fragment {

    public Author3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_author3, container, false);

        ImageView fata = (ImageView)rootView.findViewById(R.id.fata);
        fata.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fata.unam.mx/"));
                getActivity().startActivity(intent);
            }
        });

        ImageView google_author2 = (ImageView)rootView.findViewById(R.id.google_author3);
        google_author2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/111269633514395382909"));
                getActivity().startActivity(intent);
            }
        });

        ImageView unam = (ImageView)rootView.findViewById(R.id.unam);
        unam.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.unam.mx/"));
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
}