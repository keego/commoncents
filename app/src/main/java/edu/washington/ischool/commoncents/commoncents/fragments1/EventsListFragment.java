package edu.washington.ischool.commoncents.commoncents.fragments1;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.washington.ischool.commoncents.commoncents.DataRepository;
import edu.washington.ischool.commoncents.commoncents.activites1.EventSummaryActivity;
import edu.washington.ischool.commoncents.commoncents.activites1.AddEventActivity;
import edu.washington.ischool.commoncents.commoncents.adapters1.EventsListAdapter;
import edu.washington.ischool.commoncents.commoncents.AppState;
import edu.washington.ischool.commoncents.commoncents.models1.Event;
import edu.washington.ischool.commoncents.commoncents.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragment extends Fragment implements EventsListAdapter.Listener {

    static String TAG = "EventsListFragment";

    private EventsListAdapter adapter;

    //----------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    //----------------------------------------------------------------------------------------------

    public EventsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_events_list, container, false);
        initializeFab(getContext(), mainView);
        initializeEventsList(mainView);

        DataRepository.getInstance().subscribeToEventCollectionUpdates(adapter);

        return mainView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DataRepository.getInstance().unsubscribeFromEventCollectionUpdates(adapter);
    }

    //----------------------------------------------------------------------------------------------
    // Fragment Implementation
    //----------------------------------------------------------------------------------------------

    private void initializeFab(final Context context, View mainView) {
        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddEventActivity.class));
            }
        });
    }

    private void initializeEventsList(View mainView) {
        // Get the recycler
        RecyclerView eventsList = (RecyclerView) mainView.findViewById(R.id.events_list);

        // Set some basic properties
        eventsList.setHasFixedSize(true);

        // Use a simple linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventsList.setLayoutManager(layoutManager);

        // Create the adapter
        adapter = new EventsListAdapter(getContext(), this, DataRepository.getInstance().getEvents());
        eventsList.setAdapter(adapter);
    }

    //----------------------------------------------------------------------------------------------
    // EventsListAdapter.Listener Implementation
    //----------------------------------------------------------------------------------------------

    @Override
    public void onEventClicked(View view, Event event) {
        // TODO: go to detail activity
        Toast.makeText(getContext(), "TODO: show details for " + event.getName(), Toast.LENGTH_SHORT).show();
        AppState.getCurrentState().selectEvent(event);
        startActivity(new Intent(getActivity(), EventSummaryActivity.class));
    }

    @Override
    public void onEventLongClicked(View view, Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_caution)
                .setTitle(R.string.warning_title)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Delete feature coming soon!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public List<Event> getUpdatedDataSet() {
        return DataRepository.getInstance().getEvents();
    }
}
