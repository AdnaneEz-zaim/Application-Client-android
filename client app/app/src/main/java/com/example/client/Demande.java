package com.example.client;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.Vector;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Demande extends Fragment {
    private Button b;
    private View view;
    private Context context;
    private SessionManager sessionManager;
    private Client client;
    private MyRequest myRequest;
    private RequestQueue queue;
    private MyRequest request;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_demande, container, false);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe);
        queue = VolleySinglton.getInstance(getActivity()).getRequestQueue();
        request=new MyRequest(getActivity(),queue);
        sessionManager=new SessionManager(getActivity());

        if (sessionManager.isLogged())
        {
            client = sessionManager.getClient();
            client.clearDemandes();
            request.loadProductsDemandes(client, new MyRequest.PDCallBack() {
                @Override
                public void onSuccess(Client client) {
                    sessionManager.insertClient(client);
                    final ArrayList<ProduitDemande> arrayList = new ArrayList<ProduitDemande>();
                    Vector<ProdutsAllreadyDemande> prods = client.getProductsDemande();
                    for (int i = 0; i <prods.size() ; i++) {
                        ProdutsAllreadyDemande prod = prods.get(i);
                        arrayList.add(new ProduitDemande(prod.getNomProduit(),prod.getQuantiteProduit(),prod.isChecked(),prod.getImagePath(),prod.getPrixProduit(),prod.getPromo()));
                    }

                    final ProduitDemandeAdapter adapter = new ProduitDemandeAdapter(getActivity(), arrayList);
                    ListView listView = (ListView) view.findViewById(R.id.listview);
                    listView.setAdapter(adapter);
                    adapter.setOnItemDeleteListner(new ProduitDemandeAdapter.onDeleteListner() {
                        @Override
                        public void onDelete(int position) {
                            arrayList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onErr(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            });

            client = sessionManager.getClient();
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    client.clearDemandes();
                    request.loadProductsDemandes(client, new MyRequest.PDCallBack() {
                        @Override
                        public void onSuccess(Client client) {
                            sessionManager.insertClient(client);
                            ArrayList<ProduitDemande> arrayList = new ArrayList<ProduitDemande>();
                            Vector<ProdutsAllreadyDemande> prods = client.getProductsDemande();
                            for (int i = 0; i <prods.size() ; i++) {
                                ProdutsAllreadyDemande prod = prods.get(i);
                                arrayList.add(new ProduitDemande(prod.getNomProduit(),prod.getQuantiteProduit(),prod.isChecked(),prod.getImagePath(),prod.getPrixProduit(),prod.getPromo()));
                            }

                            ProduitDemandeAdapter adapter = new ProduitDemandeAdapter(getActivity(), arrayList);

                            ListView listView = (ListView) view.findViewById(R.id.listview);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onErr(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    });

                    client = sessionManager.getClient();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },4000);
                }
            });
        }



        b = view.findViewById(R.id.ajouterDemande);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AjouterDemande.class));
            }
        });


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        request.kill();
    }
}