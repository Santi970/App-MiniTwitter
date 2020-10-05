package com.example.minitwitter.ui.tweets;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

//Clase que nos permite gestionar y dibujar la interfaz de usuario de la lista de tweet o de un tweets. El FRAGEMENT TWEET.

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private List<Tweet> mValues;
    String username;
    TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context contexto, List<Tweet> items) {
        mValues = items;
        ctx = contexto;
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME);
        tweetViewModel = new ViewModelProvider((FragmentActivity)ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {    //Carga el layout sobre el uqe vamos a diseniar un elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {    //metodo que se encarga de ir dibujando cada elemento de la lista sobre el recylerView
        if (mValues != null) {

            holder.mItem = mValues.get(position);

            holder.tvUsername.setText("@" + holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size())); //size nos devuelve el tamaño del array y con eso sabriamos el num de likes.

            String photo = holder.mItem.getUser().getPhotoUrl();
            if (!photo.equals("")) {
                Glide.with(ctx)       //Con esta linea cargamos la imagen en el ivAvatar si es qeu el usuario tiene foto de perfil.
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                        .into(holder.ivAvatar);
            }

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            holder.tvLikesCount.setTextColor(ctx.getResources().getColor(android.R.color.black));  //Aplicando el color rosa a nuestro texView del numero de likes.
            holder.tvLikesCount.setTypeface(null, Typeface.NORMAL);

            holder.ivShowMenu.setVisibility(View.GONE);
            if (holder.mItem.getUser().getUsername().equals(username)){
                    holder.ivShowMenu.setVisibility(View.VISIBLE);
            }

            holder.ivShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweetViewModel.openDialogTweetMenu(ctx,holder.mItem.getId());
                }
            });

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

            for (Like like : holder.mItem.getLikes()) {
                if (like.getUsername().equals(username)) {
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.ivLike);
                    holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));  //Aplicando el color rosa a nuestro texView del numero de likes.
                    holder.tvLikesCount.setTypeface(null, Typeface.BOLD);   //Ponemos en negrita el texto del que le ponemos like.
                    break;
                }
            }
        }
    }

    public void setData(List<Tweet> tweetsList){
       this.mValues =   tweetsList;
       notifyDataSetChanged();    //Metodo que nos permite contabilizar el numemro de la lista.
    }

    @Override
    public int getItemCount() {
        if (mValues != null)    //si mValues es distinto volvemos el tamanio de lista.
        return mValues.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final ImageView ivShowMenu;
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar =view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
            ivShowMenu = view.findViewById(R.id.imageViewShowMenu);
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvMessage = view.findViewById(R.id.textViewMessage);
            tvLikesCount = view.findViewById(R.id.textViewLikes);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUsername.getText() + "'";
        }
    }
}