package mx.unam.cfata.labsampleanalyser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Author2 extends Fragment {

    public Author2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_author1, container, false);

        ImageView github = (ImageView)rootView.findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jlrodriguezf"));
                getActivity().startActivity(intent);
            }
        });

        ImageView google = (ImageView)rootView.findViewById(R.id.google);
        google.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/116908567238395484216"));
                getActivity().startActivity(intent);
            }
        });

        ImageView website = (ImageView)rootView.findViewById(R.id.website);
        website.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jlrodriguezf.github.io"));
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }
}