package de.htwg.co2footprint_tracker.views.tips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.htwg.co2footprint_tracker.R;
import de.htwg.co2footprint_tracker.databinding.FragmentTipBinding;
import de.htwg.co2footprint_tracker.model.TipItem;

/**
 * A fragment representing a list of Items.
 */
public class TipsFragment extends Fragment {

    private FragmentTipBinding binding;

    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;

    /**
     * Returns a new instance of the tips fragment.
     */
    public static TipsFragment getInstance() {
        return new TipsFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tip,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        // initializing the listeners
        initListeners();
        // initializing the objects
        initObjects();
        // preparing list data
        //initListData();
    }

    /**
     * method to initialize the views
     */
    private void initViews() {
        expandableListView = getView().findViewById(R.id.expandable_tip_list);
    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {
        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < listDataGroup.size(); i++) {
                    if (groupPosition != i) expandableListView.collapseGroup(i);
                }
            }
        });
        // ExpandableListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
    }

    /**
     * method to initialize the objects
     */
    private void initObjects() {
        // initializing the list of groups
        listDataGroup = new ArrayList<>();
        // initializing the list of child
        listDataChild = new HashMap<>();
        // initializing the adapter object
        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), listDataGroup, listDataChild);
        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);
    }

    /*
     * Preparing the list data
     *
     * Dummy Items
     */
    private void initListData() {

        String object = FirebaseRemoteConfig.getInstance().getString("tips");
        Gson gson = new GsonBuilder().create();
        List<TipItem> tipItemList = gson.fromJson(object, new TypeToken<List<TipItem>>() {
        }.getType());
        // Adding group data

        for (int i = 0; i < tipItemList.size(); i++) {
            TipItem tip = tipItemList.get(i);
            listDataGroup.add(tip.getTitle());

            List<String> child = new ArrayList<>();
            child.add(tip.getDescription());
            listDataChild.put(listDataGroup.get(i), child);
        }
        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }
}

