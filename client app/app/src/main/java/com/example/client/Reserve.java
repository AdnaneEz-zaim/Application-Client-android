package com.example.client;


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
public class Reserve extends Fragment {
    View view;
    Button b;
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

        view = inflater.inflate(R.layout.fragment_reserve, container, false);


        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe);
        queue = VolleySinglton.getInstance(getActivity()).getRequestQueue();
        request=new MyRequest(getActivity(),queue);
        sessionManager=new SessionManager(getActivity());
        if (sessionManager.isLogged())
        {
            client = sessionManager.getClient();
            request.loadProductsReserves(client, new MyRequest.PDCallBack() {
                @Override
                public void onSuccess(Client iclient) {
                    sessionManager.insertClient(iclient);
                    ArrayList<ProduitReserve> arrayList = new ArrayList<ProduitReserve>();
                    Vector<ProductsAllreadyReserved> prods = iclient.getProductsReserve();
                    for (int i = 0; i <prods.size() ; i++) {
                        ProductsAllreadyReserved prod = prods.get(i);
                        String[] date = prod.getDate().split("-");
                        arrayList.add(new ProduitReserve(prod.getNomProduit(),prod.getQuantiteProduit(),prod.isCheckedConfirme(),prod.isCheckedDecline(),prod.getImagePath(),prod.getPrixProduit(),Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0])));
                    }
                    ProduitReserveAdapter adapter = new ProduitReserveAdapter(getActivity(), arrayList);
                    ListView listView = (ListView) view.findViewById(R.id.listviewReserve);
                    listView.setAdapter(adapter);

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
                    request.loadProductsReserves(client, new MyRequest.PDCallBack() {
                        @Override
                        public void onSuccess(Client iclient) {
                            sessionManager.insertClient(iclient);
                            ArrayList<ProduitReserve> arrayList = new ArrayList<ProduitReserve>();
                            Vector<ProductsAllreadyReserved> prods = iclient.getProductsReserve();
                            for (int i = 0; i <prods.size() ; i++) {
                                ProductsAllreadyReserved prod = prods.get(i);
                                String[] date = prod.getDate().split("-");
                                arrayList.add(new ProduitReserve(prod.getNomProduit(),prod.getQuantiteProduit(),prod.isCheckedConfirme(),prod.isCheckedDecline(),prod.getImagePath(),prod.getPrixProduit(),Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0])));
                            }
                            ProduitReserveAdapter adapter = new ProduitReserveAdapter(getActivity(), arrayList);
                            ListView listView = (ListView) view.findViewById(R.id.listviewReserve);
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




        b = view.findViewById(R.id.ajouterReservation);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AjouterReservation.class));
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