package com.screens.flows.profile.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.components.SettingsNavBar.SettingsNavBar;
import com.components.buttons.DebounceClickListener;
import com.components.navMenu.BottomNavMenu;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentProfileBinding;
import com.screens.flows.home.vm.HomeViewModel;
import com.screens.flows.profile.vm.ProfileViewModel;
import com.screens.flows.translate.vm.TranslateViewModel;
import com.screens.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ProfileFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentProfileBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;
    private TranslateViewModel translateViewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    private VideoFavoriteAdapter videoFavoriteAdapter;
    private TranslateFavoriteAdapter translateFavoriteAdapter;

    ArrayList<ObjTraFav> traduccionesFavoritas;

    ArrayList<ObjVideoFav> videosFavoritos;

    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        translateViewModel = new ViewModelProvider(requireActivity()).get(TranslateViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        setListeners();
        setupSwipeToDelete(binding.rvFavoritos);
        servicioPerfil();

    }

    @Override
    public void onResume() {
        super.onResume();
        ProfileViewModel.setBottomNavVisible(true);
        ProfileViewModel.selectTab(BottomNavMenu.TAB_PROFILE);
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setListeners(){
        binding.navBar.setOnTabSelectedListener((tabId) ->{
            switch (tabId) {
                // Tab video
                case SettingsNavBar.VIDEO:
                    binding.rvFavoritos.setAdapter(videoFavoriteAdapter);
                    break;

                // Tab translate
                case SettingsNavBar.TRANSLATE:
                    binding.rvFavoritos.setAdapter(translateFavoriteAdapter);
                    break;
            }
        });

        binding.imgSettings.setOnClickListener(new DebounceClickListener(view -> {
            navigateTo(binding.getRoot(), R.id.action_profileFragment_to_settingsFragment, null);

        }));

        binding.llScore.setOnClickListener(new DebounceClickListener(view -> {
            sharedPreferencesManager = new SharedPreferencesManager(requireContext());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            String todayDate = dateFormat.format(new Date());

            String lastShownDate = sharedPreferencesManager.getLastChallengeShowDate();
            if (todayDate.equals(lastShownDate) && sharedPreferencesManager.isChallengeShow() ) { // TODO QUITAR, EJEMPLO PARA DEMO
                navigateTo(binding.getRoot(), R.id.action_profileFragment_to_challengeFragment, null);

            }else{
                Toast.makeText(getContext(), "Ya has completado tu reto diario", Toast.LENGTH_SHORT).show();

            }

        }));

    }

    private void setViewData(String name, String score, String image){

        binding.tvName.setText(name);
        binding.tvScore.setText(score);

        try {
            int resourceId = getResources().getIdentifier(image, "drawable", getActivity().getPackageName());
            if (resourceId != 0) {
                binding.imgProfile.setImageDrawable(getResources().getDrawable(resourceId));
            } else {
                binding.imgProfile.setImageDrawable(getResources().getDrawable(com.components.R.drawable.q_azul));
            }
        } catch (Exception e) {
            e.printStackTrace();
            binding.imgProfile.setImageDrawable(getResources().getDrawable(com.components.R.drawable.q_azul));
        }

    }

    private void setVideoAdapter(ArrayList<ObjVideoFav> videoFavorites) {
        videoFavoriteAdapter = new VideoFavoriteAdapter(getContext(), videoFavorites, video -> {
            homeViewModel.selectVideo(video);
            ProfileViewModel.selectTab(BottomNavMenu.TAB_HOME);
        });

        binding.rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvFavoritos.getContext(), LinearLayoutManager.VERTICAL);
        binding.rvFavoritos.addItemDecoration(dividerItemDecoration);

    }



    private void setTranslateAdapter(ArrayList<ObjTraFav> traduccionesFav) {
        translateFavoriteAdapter = new TranslateFavoriteAdapter(getContext(), traduccionesFav, traduccion -> {
            translateViewModel.selectTrans(traduccion);
            ProfileViewModel.selectTab(BottomNavMenu.TAB_TRANSLATE);
        });

        binding.rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvFavoritos.getContext(), LinearLayoutManager.VERTICAL);
        binding.rvFavoritos.addItemDecoration(dividerItemDecoration);

    }


    private void inicializarAdapter(){
        if(profileViewModel.getShowVideoFavorite()){
            binding.rvFavoritos.setAdapter(videoFavoriteAdapter);

        }else{
            binding.rvFavoritos.setAdapter(translateFavoriteAdapter);
            binding.navBar.setActiveTab(com.components.R.id.imgTranslate);
            profileViewModel.setShowVideoFavorite(true);
        }
    }

    private void setupSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                // Determina cuál lista está activa basándose en el adaptador actual del RecyclerView
                if (recyclerView.getAdapter() instanceof VideoFavoriteAdapter) {
                    videosFavoritos.remove(position);  // Remover de videos favoritos
                    recyclerView.getAdapter().notifyItemRemoved(position);

                    // TODO: Llamar al servicio para eliminar video
                } else if (recyclerView.getAdapter() instanceof TranslateFavoriteAdapter) {
                    traduccionesFavoritas.remove(position);  // Remover de traducciones favoritas
                    recyclerView.getAdapter().notifyItemRemoved(position);

                    // TODO: Llamar al servicio para eliminar traducción
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                // Dibujar el ícono de borrado y fondo rojo
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                c.drawRect(background, paint);

                Drawable deleteIcon = ContextCompat.getDrawable(getContext(), com.components.R.drawable.basurero);
                int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + iconMargin;
                int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                deleteIcon.draw(c);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }




    // Servicios -----------------------------------------------------------------------------------

    private void servicioPerfil(){
        // TODO SERVICIO Y OBJETO

        // ON SUCEES SETAR VALORES
        String nombre = "Ejemplo";
        String racha = "10";
        String imagen = "q_azul";

        setViewData(nombre, racha, imagen);
        servicioVideosFavoritos();

    }

    private void  servicioVideosFavoritos(){

        // TODO SERVICIO
        // ON SUCESS

        // TODO VIDEO OBTENER PRIMERA IMAGEN

        ArrayList<ObjVideoFav> videoFavorites = new ArrayList<>();
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));

        this.videosFavoritos = videoFavorites;
        setVideoAdapter(videoFavorites);
        servicioTraduccionesFavoritas();
    }


    private void servicioTraduccionesFavoritas(){
        // TODO SERVICIO
        // ON SUCESS

        ArrayList<ObjTraFav> traduccionesFavoritas = new ArrayList<>();
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));

        this.traduccionesFavoritas = traduccionesFavoritas;
        setTranslateAdapter(traduccionesFavoritas);
        inicializarAdapter();

    }
}