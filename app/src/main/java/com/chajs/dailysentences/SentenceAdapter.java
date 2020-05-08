package com.chajs.dailysentences;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.MyViewHolder> {

    private ArrayList<Sentence> mySentenceList = null;

    /*
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
     */

    // 리스너 객체 참조를 저장하는 변수
    //private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    /*
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }
     */

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDateInfo;
        TextView txtSentence;
        TextView txtSCountInfo;
        TextView txtFCountInfo;
        TextView txtKCountInfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtDateInfo = (TextView) itemView.findViewById(R.id.textViewDateInfo);
            this.txtSentence = (TextView) itemView.findViewById(R.id.textViewSentence);
            this.txtSCountInfo = (TextView) itemView.findViewById(R.id.textViewSCountInfo);
            this.txtFCountInfo = (TextView) itemView.findViewById(R.id.textViewFCountInfo);
            this.txtKCountInfo = (TextView) itemView.findViewById(R.id.textViewKCountInfo);

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
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView textDateInfo = holder.txtDateInfo;
        TextView textSentence = holder.txtSentence;
        TextView textSCountInfo = holder.txtSCountInfo;
        TextView textFCountInfo = holder.txtFCountInfo;
        TextView textKCountInfo = holder.txtKCountInfo;

        textDateInfo.setText("Noti time: " + mySentenceList.get(position).getPopupTime() + "    Duration: " +
                mySentenceList.get(position).getStartDate() + " - " +
                mySentenceList.get(position).getEndDate());
        textSentence.setText(mySentenceList.get(position).getKorSentence());
        textSCountInfo.setText("Success:" + mySentenceList.get(position).getSucessCount());
        textFCountInfo.setText("Fail:" + mySentenceList.get(position).getFailCount());
        textKCountInfo.setText("Skip:" + mySentenceList.get(position).getSkipCount());
    }

    @Override
    public int getItemCount() {
        return mySentenceList.size();
    }


}
