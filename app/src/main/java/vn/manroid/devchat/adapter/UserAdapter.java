package vn.manroid.devchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import vn.manroid.devchat.R;
import vn.manroid.devchat.model.UserModel;

/**
 * Created by manro on 12/06/2017.
 */


public class UserAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<UserModel> listUser;
    private Context context;
    private RequestOptions requestOptions;

    public UserAdapter(Context context, LayoutInflater inflater, ArrayList<UserModel> listUser) {
        this.inflater = inflater;
        this.listUser = listUser;
        this.context = context;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.userapp);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.error(R.drawable.userapp);
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public UserModel getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_user, null);
        }

        final ImageView imgAvatarUser = view.findViewById(R.id.imgAvatar);
        ImageView imgStatus = view.findViewById(R.id.imgStatus);
        TextView txtUserName = view.findViewById(R.id.txtUserName);

        UserModel model = getItem(position);

//        LoadImageFromFacebook facebook = new LoadImageFromFacebook();
//        facebook.execute(model.getPhotoURL());

        Glide.with(context).setDefaultRequestOptions(requestOptions.override(200, 200))
                .asBitmap().
                load(model.getPhotoURL()).
                into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                        imgAvatarUser.setImageBitmap(resource);
                    }
                });


        txtUserName.setText(model.getName());

        model.setCurrentPosition(position);

//        imgAvatarUser.setImageBitmap(facebook.getBitmapAvatar(position));

        if (model.isOnline()) {
            imgStatus.setImageResource(R.drawable.online);
        } else {
            imgStatus.setImageResource(R.drawable.offline);
        }

        return view;
    }
}