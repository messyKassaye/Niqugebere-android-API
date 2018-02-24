package com.example.meseret.niqugebere.profile.transporter.transporterFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.transporter.transporterClient.TransportClient;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Vehicles;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVehiclesFragment extends Fragment {
    private ViewFlipper flipper;
    private View first_view,secondview;


    private EditText plate_no_edt,capacity_edt;
    private Button register_btn;
    private TextView error_shower;
    private String token;
    private SharedPreferences preferences;
    private ProgressBar pr;

    private TextView registration_succeeded;
    private Button add_more_btn;
    public AddVehiclesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_vehicles, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));

        flipper=(ViewFlipper)view.findViewById(R.id.add_vehicles_flipper);

        first_view=LayoutInflater.from(getActivity()).inflate(R.layout.add_vehicles_firstview,null);
        secondview=LayoutInflater.from(getActivity()).inflate(R.layout.add_vehicles_second_view,null);

        plate_no_edt=(EditText)first_view.findViewById(R.id.vehicle_plate_no);
        capacity_edt=(EditText)first_view.findViewById(R.id.vehicle_capacity);
        error_shower=(TextView)first_view.findViewById(R.id.add_vehicles_error_text);
        register_btn=(Button)first_view.findViewById(R.id.add_vehicles_btn);

        registration_succeeded=(TextView)secondview.findViewById(R.id.add_vehicles_success_message);
        add_more_btn=(Button)secondview.findViewById(R.id.add_more_vehicles);

        pr=(ProgressBar)first_view.findViewById(R.id.add_vehicles_pr);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String plate_no_values=plate_no_edt.getText().toString();
                final String capacity_value= capacity_edt.getText().toString();
                if(plate_no_values.equals("")){
                   error_shower.setText("Please add your vehicle plate number");
                    error_shower.setVisibility(View.VISIBLE);
                }else if(capacity_value.equals("")){
                   error_shower.setText("Please add your vehicles capacity");
                    error_shower.setVisibility(View.VISIBLE);
                }else {
                    pr.setVisibility(View.VISIBLE);
                    Vehicles vehicles=new Vehicles(plate_no_values,capacity_value);
                    Retrofit retrofit=getUserAPIretrofit();
                    TransportClient client=retrofit.create(TransportClient.class);
                    Call<ResponseToken> call=client.registerVehicles(getToken(),vehicles);
                    call.enqueue(new Callback<ResponseToken>() {
                        @Override
                        public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                            if(response.isSuccessful()){
                                pr.setVisibility(View.GONE);
                                if(response.body().getStatus().equals("ok")){
                                    flipper.removeView(first_view);
                                    flipper.addView(secondview);
                                    flipper.showNext();
                                    plate_no_edt.setText("");
                                    capacity_edt.setText("");
                                    error_shower.setText("");
                                    registration_succeeded.setText("Congratulations you have added your car with plate no  "+plate_no_values);

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseToken> call, Throwable t) {

                        }
                    });
                }
            }
        });

        add_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.removeView(secondview);
                flipper.addView(first_view);
                flipper.showNext();
            }
        });
        flipper.addView(first_view);
        flipper.showNext();

        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
        // set the animation type's to ViewFlipperActivity
        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);
        // set interval time for flipping between views
        flipper.setFlipInterval(3000);
        flipper.setDisplayedChild(6);

        return  view;
    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
