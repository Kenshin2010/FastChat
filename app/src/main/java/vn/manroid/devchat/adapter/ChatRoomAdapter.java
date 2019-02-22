package vn.manroid.devchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.Random;

import vn.manroid.devchat.R;
import vn.manroid.devchat.custom.CustomView;
import vn.manroid.devchat.model.ChatRoomModel;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRooomHolder> {

    private List<ChatRoomModel> chatRoomModelList;
    private RequestOptions requestOptions;
    private Context context;

    public ChatRoomAdapter(List<ChatRoomModel> chatRoomModelList, Context context) {
        this.chatRoomModelList = chatRoomModelList;
        this.context = context;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.userapp);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.error(R.drawable.userapp);

    }

    @Override
    public ChatRooomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_room_item, parent, false);
        return new ChatRooomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatRooomHolder holder, int position) {
        ChatRoomModel chat = chatRoomModelList.get(position);

        //load image
        Glide.with(context).setDefaultRequestOptions(requestOptions.override(200, 200))
                .asBitmap().
                load(chat.getPhotoUserChatRoom()).
                into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                        holder.customView.setImageBitmap(resource);
                        holder.customView.setClickable(true);
                    }
                });


        if (chat.getIdTenGuiRoom() == null) {
            if (chat.getThongDiepRoom() != null)
                holder.textView.setText("Người lạ" + " : \n" + chat.getThongDiepRoom());
            else
                holder.textView.setText("Người lạ" + " : \n" + "Tin nhắn bị lỗi");
        } else {
            if (chat.getThongDiepRoom() != null)
                holder.textView.setText(chat.getIdTenGuiRoom() + " : \n" + chat.getThongDiepRoom());
            else
                holder.textView.setText(chat.getIdTenGuiRoom() + " : \n" + "Tin nhắn bị lỗi");
        }


        Random r = new Random();
        int num = r.nextInt(10);

        switch (num) {
            case 0:
                holder.textView.setTextColor(context.getResources().getColor(R.color.xanhdatroi));
                break;
            case 1:
                holder.textView.setTextColor(context.getResources().getColor(R.color.mautim));
                break;
            case 2:
                holder.textView.setTextColor(context.getResources().getColor(R.color.maudo));
                break;
            case 3:
                holder.textView.setTextColor(context.getResources().getColor(R.color.mauhong));
                break;
            case 4:
                holder.textView.setTextColor(context.getResources().getColor(R.color.maunau));
                break;
            case 5:
                holder.textView.setTextColor(context.getResources().getColor(R.color.vang));
                break;

        }

    }

    public List<ChatRoomModel> getListData() {
        return chatRoomModelList;
    }

    @Override
    public int getItemCount() {
        return chatRoomModelList.size();
    }

    public class ChatRooomHolder extends RecyclerView.ViewHolder {
        private CustomView customView;
        private TextView textView;

        public ChatRooomHolder(View view) {
            super(view);
            customView = view.findViewById(R.id.imgAvatarChattingRoom);
            textView = view.findViewById(R.id.txtTextChatRoom);
        }
    }
}