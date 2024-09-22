package com.screens.flows.profile.ui;

import android.content.Context;
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

import android.os.Handler;
import android.os.Looper;
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
import com.screens.flows.video.vm.VideoViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoRequest;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoResponse;
import com.senaschapinas.flows.GetUserInfo.ObjTraFav;
import com.senaschapinas.flows.GetUserInfo.ObjVideoFav;
import com.senaschapinas.flows.RemoveTraduction.RemoveTraductionRequest;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ProfileFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentProfileBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;
    private VideoViewModel videoViewModel;
    private TranslateViewModel translateViewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    private VideoFavoriteAdapter videoFavoriteAdapter;
    private TranslateFavoriteAdapter translateFavoriteAdapter;

    List<ObjTraFav> traduccionesFavoritas;

    List<ObjVideoFav> videosFavoritos;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private int remainingImagesToLoad = 0;


    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        translateViewModel = new ViewModelProvider(requireActivity()).get(TranslateViewModel.class);
        videoViewModel = new ViewModelProvider(requireActivity()).get(VideoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

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
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setListeners(){
        binding.navBar.setOnTabSelectedListener((tabId) -> {
            switch (tabId) {
                // Tab video
                case SettingsNavBar.VIDEO:
                    if (videosFavoritos == null || videosFavoritos.isEmpty()) {
                        toggleEmptyStateForFavorites(true, true);
                    } else {
                        toggleEmptyStateForFavorites(false, true);
                        binding.rvFavoritos.setAdapter(videoFavoriteAdapter);
                    }
                    break;

                // Tab translate
                case SettingsNavBar.TRANSLATE:
                    if (traduccionesFavoritas == null || traduccionesFavoritas.isEmpty()) {
                        toggleEmptyStateForFavorites(true, false);
                    } else {
                        toggleEmptyStateForFavorites(false, false);
                        binding.rvFavoritos.setAdapter(translateFavoriteAdapter);
                    }
                    break;
            }
        });


        binding.imgSettings.setOnClickListener(new DebounceClickListener(view -> {
            navigateTo(binding.getRoot(), R.id.action_profileFragment_to_settingsFragment, null);

        }));

        binding.llScore.setOnClickListener(new DebounceClickListener(view -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            String todayDate = dateFormat.format(new Date());

            String lastShownDate = sharedPreferencesManager.getLastChallengeShowDate();
            if (todayDate.equals(lastShownDate) && sharedPreferencesManager.isChallengeShow() ) {
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

    private void setVideoAdapter(List<ObjVideoFav> videoFavorites) {
        videoFavoriteAdapter = new VideoFavoriteAdapter(getContext(), videoFavorites, video -> {
            homeViewModel.selectVideo(video);
                downloadVideo(video);
        });

        binding.rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvFavoritos.getContext(), LinearLayoutManager.VERTICAL);
        binding.rvFavoritos.addItemDecoration(dividerItemDecoration);

    }



    private void setTranslateAdapter(List<ObjTraFav> traduccionesFav) {
        translateFavoriteAdapter = new TranslateFavoriteAdapter(getContext(), traduccionesFav, traduccion -> {
            translateViewModel.selectTrans(traduccion);
            ProfileViewModel.selectTab(BottomNavMenu.TAB_TRANSLATE);
        });

        binding.rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvFavoritos.getContext(), LinearLayoutManager.VERTICAL);
        binding.rvFavoritos.addItemDecoration(dividerItemDecoration);

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
                    servicioEliminarVideoFavorito(position, recyclerView);

                } else if (recyclerView.getAdapter() instanceof TranslateFavoriteAdapter) {
                    servicioEliminarTraduccionFavorita(position, recyclerView);
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


    private void toggleEmptyStateForFavorites(boolean isEmpty, boolean isVideo) {
        if (isEmpty) {
            binding.rvFavoritos.setVisibility(View.GONE);
            binding.emptystate.setVisibility(View.VISIBLE);
            binding.tvEmpty.setText(isVideo ? R.string.empty_video : R.string.empty_trad);
        } else {
            binding.rvFavoritos.setVisibility(View.VISIBLE);
            binding.emptystate.setVisibility(View.GONE);
        }
    }

    private void paintData(Resource<GetUserInfoResponse> resource){

        // Data
        String nombre = "";
        String racha = "";
        String imagen = "";

        if(resource.data != null ){
            if(resource.data.getEmail()!= null){
                nombre  = resource.data.getEmail().split("@")[0];
                profileViewModel.setNombre(resource.data.getEmail());
            }

            racha = String.valueOf(resource.data.getStreak());
            profileViewModel.setRacha(racha);

            if(resource.data.getQuetzalito() != null){
                imagen = resource.data.getQuetzalito();
            }

            if(!resource.data.getVideosFav().isEmpty()){
                this.videosFavoritos = resource.data.getVideosFav();
                setVideoAdapter(this.videosFavoritos);
            }

            if(!resource.data.getTraductionsFav().isEmpty()){
                this.traduccionesFavoritas = resource.data.getTraductionsFav();
                setTranslateAdapter(this.traduccionesFavoritas);
            }
        }

        setViewData(nombre, racha, imagen);


        // Navegacion
        if (profileViewModel.getShowVideoFavorite()) {
            if (videosFavoritos == null || videosFavoritos.isEmpty()) {
                toggleEmptyStateForFavorites(true, true);
            } else {
                toggleEmptyStateForFavorites(false, true);
                binding.rvFavoritos.setAdapter(videoFavoriteAdapter);
            }
        } else {
            binding.navBar.setActiveTab(com.components.R.id.imgTranslate);
            profileViewModel.setShowVideoFavorite(true);

            if (traduccionesFavoritas == null || traduccionesFavoritas.isEmpty()) {
                toggleEmptyStateForFavorites(true, false);
            } else {
                toggleEmptyStateForFavorites(false, false);
                binding.rvFavoritos.setAdapter(translateFavoriteAdapter);
            }
        }


    }







    // Servicios -----------------------------------------------------------------------------------

    private void servicioPerfil(){

        showCustomDialogProgress(requireContext());

        GetUserInfoRequest request = new GetUserInfoRequest();
        request.setIdUser(sharedPreferencesManager.getIdUsuario());

        profileViewModel.fetchUserInfo(request);
        profileViewModel.getUserInfoResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();

                        paintData(resource);

                        break;
                    case ERROR:
                        hideCustomDialogProgress();
                        showCustomDialogMessage(
                                resource.message,
                                "Oops algo salió mal",
                                "",
                                "Cerrar",
                                null,
                                ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                        );
                        break;

                }
            }
        });

    }


    private void servicioEliminarVideoFavorito(int position, RecyclerView recyclerView) {
        showCustomDialogProgress(requireContext());

        RemoveVideoRequest request = new RemoveVideoRequest();
        request.setId_video(videosFavoritos.get(position).getId_video());
        request.setId_user(sharedPreferencesManager.getIdUsuario());

        videoViewModel.removeVideo(request);
        videoViewModel.getRemoveVideoResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();
                        videosFavoritos.remove(position);
                        recyclerView.getAdapter().notifyItemRemoved(position);

                        break;
                    case ERROR:
                        hideCustomDialogProgress();
                        showCustomDialogMessage(
                                resource.message,
                                "Oops algo salió mal",
                                "",
                                "Cerrar",
                                null,
                                ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                        );
                        recyclerView.getAdapter().notifyItemChanged(position);

                        break;

                }
            }
        });
    }


    private void servicioEliminarTraduccionFavorita(int position, RecyclerView recyclerView) {
        showCustomDialogProgress(requireContext());

        RemoveTraductionRequest request  = new RemoveTraductionRequest();
        request.setId_sentence(traduccionesFavoritas.get(position).getId_traduction());
        request.setId_user(sharedPreferencesManager.getIdUsuario());

        translateViewModel.removeTranslation(request);
        translateViewModel.getRemoveTranslationResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();
                        traduccionesFavoritas.remove(position);
                        recyclerView.getAdapter().notifyItemRemoved(position);

                        break;
                    case ERROR:
                        hideCustomDialogProgress();
                        showCustomDialogMessage(
                                resource.message,
                                "Oops algo salió mal",
                                "",
                                "Cerrar",
                                null,
                                ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                        );
                        recyclerView.getAdapter().notifyItemChanged(position);

                        break;

                }
            }
        });
    }



    public void downloadVideo(ObjVideoFav videoFav) {
        showCustomDialogProgress(requireContext());

        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(videoFav.getLink_video()).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String videoPath = saveVideoToFile(requireContext(), response.body());
                    if (videoPath != null) {
                        // Pasar el resultado de vuelta al hilo principal y navegar al fragmento
                        new Handler(Looper.getMainLooper()).post(() -> {
                            hideCustomDialogProgress();
                            Bundle bundle = new Bundle();
                            bundle.putString("video_path", videoPath);
                            bundle.putString("lensegua", videoFav.getSentence_lensegua());
                            bundle.putString("espanol", videoFav.getTraduction_esp());
                            bundle.putString("id_video", videoFav.getId_video());
                            bundle.putBoolean("favorito", true);

                            navigateTo(binding.getRoot(), R.id.action_profileFragment_to_videoFragment2, bundle);
                            homeViewModel.selectVideo(null);
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            hideCustomDialogProgress();
                            showCustomDialogMessage(
                                    "Error al guardar el video.",
                                    "Oops algo salió mal",
                                    "",
                                    "Cerrar",
                                    null,
                                    ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                            );
                        });
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        hideCustomDialogProgress();
                        showCustomDialogMessage(
                                "Error al descargar el video.",
                                "Oops algo salió mal",
                                "",
                                "Cerrar",
                                null,
                                ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                        );
                    });
                }
            } catch (IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    hideCustomDialogProgress();
                    showCustomDialogMessage(
                            e.getMessage(),
                            "Oops algo salió mal",
                            "",
                            "Cerrar",
                            null,
                            ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                    );
                });
            }
        });
    }

    private String saveVideoToFile(Context context, ResponseBody body) {
        try {
            File tempFile = File.createTempFile("temp_video", ".mp4", context.getCacheDir());
            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(tempFile);
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                return tempFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





}