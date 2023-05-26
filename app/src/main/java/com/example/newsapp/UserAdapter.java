package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>
{
    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference Ref= FirebaseDatabase.getInstance().getReference("Posts");

    private Context context;
    private List<DataClass> dataList;
    public UserAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userecycleitem, parent,false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);*/
            }
            });
        isLike(dataList.get(position).getKey(),holder.Imagelike);
        nrLikes(holder.likes,dataList.get(position).getKey());
        holder.Imagelike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.Imagelike.getTag().equals("like"))
                {
                    Ref.child("Likes")
                            .child(dataList.get(holder.getAdapterPosition()).getKey()).child(firebaseUser.getUid()).setValue(true);
                }
                else
                {
                    Ref.child("Likes")
                            .child(dataList.get(holder.getAdapterPosition()).getKey()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
    public void updateDataAtIndex(int index, DataClass updatedData) {
        dataList.set(index, updatedData);
        notifyItemChanged(index);
    }

    // Method to get the position of a specific key in the dataset
   public int getPositionByKey(String key) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1; // Key not found
    }
    // ...


    public void setData(List<DataClass> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }


    public void isLike(String postid,ImageView imageView)
    {
        FirebaseUser authuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("Posts").child("Likes").child(postid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(authuser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                    imageView.setTag("Liked");
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                    imageView.setTag("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void nrLikes(TextView likes,String postid)
    {
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("Posts")
                .child("Likes").child(postid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+"likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
class UserViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, likes;
    CardView recCard;
    ImageView Imagelike;


    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        likes = itemView.findViewById(R.id.likes);

        Imagelike= itemView.findViewById(R.id.Like);
        recTitle = itemView.findViewById(R.id.recTitle);
    }
}

