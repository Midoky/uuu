/*王戌琦  2016年4月5日23:25:24
 * 功能：短信适配器
 * 详细介绍：适配个人短信
 */
package com.QingdaoUniversity.SecretText.Functions;

import java.util.List;

import com.example.uuu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MsgAdapter extends ArrayAdapter<Msg>{
	
	private int resourceId;

	public MsgAdapter(Context context,int textViewResourceId,List<Msg> objects){
		super(context , textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	@SuppressWarnings("static-access")
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		Msg msg = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
			viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
			viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
			viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
			viewHolder.lefttime = (TextView) view.findViewById(R.id.sms_time1);
			viewHolder.righttime = (TextView) view.findViewById(R.id.sms_time2);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();	
		}
		if(msg.getType() == Msg.TYPE_RECEIVED){
			//如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
			viewHolder.leftLayout.setVisibility(view.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			viewHolder.leftMsg.setText(msg.getContent());
			viewHolder.lefttime.setText(msg.getTime());
		}else if(msg.getType() == Msg.TYPE_SENT){
			//如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			viewHolder.rightMsg.setText(msg.getContent());
			viewHolder.righttime.setText(msg.getTime());
		}
		return view;
	}
	class ViewHolder{
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		
		TextView leftMsg;
		TextView lefttime;
		TextView rightMsg;
		TextView righttime;
	}

}
