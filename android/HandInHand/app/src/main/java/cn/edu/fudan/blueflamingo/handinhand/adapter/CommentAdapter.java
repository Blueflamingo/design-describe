package cn.edu.fudan.blueflamingo.handinhand.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.fudan.blueflamingo.handinhand.R;
import cn.edu.fudan.blueflamingo.handinhand.lib.AppUtility;
import cn.edu.fudan.blueflamingo.handinhand.model.ExComment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

	private List<ExComment> comments;

	private Context mContext;

	public CommentAdapter(Context context, List<ExComment> comments) {
		this.mContext = context;
		this.comments = comments;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int i) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ExComment c = comments.get(i);
		viewHolder.timeTextView.setText(simpleDateFormat.format((new Date(c.getCreatedTime()))));
		viewHolder.usernameTextView.setText(c.getNickname());
		viewHolder.contentTextView.setText(c.getContent());
		viewHolder.portraitImageView.setImageBitmap(AppUtility.getImage(c.getUserHead()));
	}

	@Override
	public int getItemCount() {
		return comments == null ? 0 : comments.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView portraitImageView;
		public TextView usernameTextView;
		public TextView contentTextView;
		public TextView timeTextView;

		public ViewHolder(View v) {
			super(v);
			portraitImageView = (ImageView) v.findViewById(R.id.comment_item_portrait);
			usernameTextView = (TextView) v.findViewById(R.id.comment_item_username);
			contentTextView = (TextView) v.findViewById(R.id.comment_item_content);
			timeTextView = (TextView) v.findViewById(R.id.comment_item_time);
		}
	}
}
