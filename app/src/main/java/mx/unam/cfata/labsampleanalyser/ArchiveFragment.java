package mx.unam.cfata.labsampleanalyser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveFragment extends Fragment {


    public ArchiveFragment() {
        // Required empty public constructor
    }


    private List<card_item> item = new ArrayList();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_archive, container, false);
        fill_card_list();

        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new card_item_adapter(item, getContext());
        recyclerView.setAdapter(adapter);

        return layout;
    }


    private void fill_card_list() {
        //TODO: Get images from gallery specific folder, and define sample name as photo name
        item.add(new card_item(R.drawable.organism_example, "LSA_1_Fecha"));
        item.add(new card_item(R.drawable.organism_example, "LSA_2_Fecha"));
        item.add(new card_item(R.drawable.organism_example, "LSA_3_Fecha"));
        item.add(new card_item(R.drawable.organism_example, "LSA_4_Fecha"));
        item.add(new card_item(R.drawable.organism_example, "LSA_5_Fecha"));
    }
}