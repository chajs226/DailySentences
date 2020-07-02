package com.chajs.dailysentences;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.MyViewHolder> {

    private ArrayList<Sentence> mySentenceList = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDateInfo;
        TextView txtSentence;
        TextView txtSCountInfo;
        TextView txtFCountInfo;
        TextView txtKCountInfo;
        TextView txtPoint;
        ImageView imgEmoji;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtDateInfo = (TextView) itemView.findViewById(R.id.textViewDateInfo);
            this.txtSentence = (TextView) itemView.findViewById(R.id.textViewSentence);
            this.txtSCountInfo = (TextView) itemView.findViewById(R.id.textViewSCountInfo);
            this.txtFCountInfo = (TextView) itemView.findViewById(R.id.textViewFCountInfo);
            this.txtKCountInfo = (TextView) itemView.findViewById(R.id.textViewKCountInfo);
            this.txtPoint = (TextView) itemView.findViewById(R.id.textViewPoint);
            this.imgEmoji = (ImageView) itemView.findViewById(R.id.imageViewEmoji);

            //아이템 클릭 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        Sentence sentence = mySentenceList.get(pos);
                        Log.d("SentenceAdapter","position: " + String.valueOf(pos));
                        Log.d("SentenceAdapter","kor: " + sentence.getKorSentence());

                        Intent intent = new Intent(v.getContext(), InsertActivity.class);
                        intent.putExtra("sentence", sentence);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }


    public SentenceAdapter(ArrayList<Sentence> dataList) {
        mySentenceList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.list_layout, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;

        /*
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;

         */
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView textDateInfo = holder.txtDateInfo;
        TextView textSentence = holder.txtSentence;
        TextView textSCountInfo = holder.txtSCountInfo;
        TextView textFCountInfo = holder.txtFCountInfo;
        TextView textKCountInfo = holder.txtKCountInfo;
        TextView textPoint = holder.txtPoint;
        ImageView imageEmoji = holder.imgEmoji;

        textDateInfo.setText("Noti time: " + mySentenceList.get(position).getPopupTime() + "    Duration: " +
                mySentenceList.get(position).getStartDate() + " - " +
                mySentenceList.get(position).getEndDate());
        textSentence.setText(mySentenceList.get(position).getKorSentence());
        textSCountInfo.setText("Success:" + mySentenceList.get(position).getSucessCount());
        textFCountInfo.setText("Fail:" + mySentenceList.get(position).getFailCount());
        textKCountInfo.setText("Skip:" + mySentenceList.get(position).getSkipCount());
        textPoint.setText((mySentenceList.get(position).getPoint())+ "Pt");
        if(Integer.parseInt(mySentenceList.get(position).getPoint()) > 10)
            imageEmoji.setImageResource(R.drawable.point_10);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > 5)
            imageEmoji.setImageResource(R.drawable.point_5);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > 0)
            imageEmoji.setImageResource(R.drawable.point_0);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > -5)
            imageEmoji.setImageResource(R.drawable.point__5);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > -7)
            imageEmoji.setImageResource(R.drawable.point__7);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > -10)
            imageEmoji.setImageResource(R.drawable.point__10);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > -15)
            imageEmoji.setImageResource(R.drawable.point__15);
        else if(Integer.parseInt(mySentenceList.get(position).getPoint()) > -20)
            imageEmoji.setImageResource(R.drawable.point__20);
        else
            imageEmoji.setImageResource(R.drawable.point__25);
    }

    @Override
    public int getItemCount() {
        return mySentenceList.size();
    }


}
