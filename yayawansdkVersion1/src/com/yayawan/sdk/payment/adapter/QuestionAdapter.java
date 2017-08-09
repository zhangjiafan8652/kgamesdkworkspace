package com.yayawan.sdk.payment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yayawan.sdk.domain.Question;
import com.yayawan.sdk.utils.ResourceUtil;
/**
 * 问题适配
 * @author wjy
 *
 */
public class QuestionAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Question> mQuestionList;
    
    class ViewHolder{
        
        TextView mTitle;
        TextView mContent;
    }
    
    public QuestionAdapter(Context context, ArrayList<Question> questionList) {
        super();
        this.mContext = context;
        this.mQuestionList = questionList;
    }

    public int getCount() {
        return mQuestionList.size();
    }

    public Object getItem(int position) {
        return mQuestionList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, ResourceUtil.getLayoutId(mContext, "question_item"), null);
            holder.mTitle = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_question_title"));
            holder.mContent = (TextView) convertView.findViewById(ResourceUtil.getId(mContext, "tv_question_content"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
       Question question = mQuestionList.get(position);
        
        holder.mTitle.setText(question.name);
        holder.mContent.setText(question.content);
        
        return convertView;
    }

}
