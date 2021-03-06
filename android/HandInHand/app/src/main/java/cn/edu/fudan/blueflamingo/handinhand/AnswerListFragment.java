package cn.edu.fudan.blueflamingo.handinhand;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fudan.blueflamingo.handinhand.adapter.AnswerAdapter;
import cn.edu.fudan.blueflamingo.handinhand.adapter.SimpleAnswerAdapter;
import cn.edu.fudan.blueflamingo.handinhand.middleware.AnswerHelper;
import cn.edu.fudan.blueflamingo.handinhand.middleware.FavoriteHelper;
import cn.edu.fudan.blueflamingo.handinhand.middleware.QuestionHelper;
import cn.edu.fudan.blueflamingo.handinhand.middleware.UserHelper;
import cn.edu.fudan.blueflamingo.handinhand.model.AnswerHeader;
import cn.edu.fudan.blueflamingo.handinhand.model.ExAnswer;
import cn.edu.fudan.blueflamingo.handinhand.model.ExQuestion;
import cn.edu.fudan.blueflamingo.handinhand.view.SwipeRefreshAndLoadLayout;

/**
 * The type Answer list fragment.
 */
public class AnswerListFragment extends Fragment {

	private RecyclerView mRecyclerView;
	private AnswerAdapter answerAdapter;
	private SimpleAnswerAdapter simpleAnswerAdapter;
	private List<ExAnswer> answers = new ArrayList<>();
	private AnswerHeader answerHeader;
	private SwipeRefreshAndLoadLayout mSwipeLayout;
	private AnswerHelper answerHelper;
	private QuestionHelper questionHelper;
	private UserHelper userHelper;
	private Global global;

	private int MODE = 0;
	private int QID = -1;
    private int UID = -1;

    /**
     * Instantiates a new Answer list fragment.
     */
    public AnswerListFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_answer_list, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		answerHelper = new AnswerHelper();
		userHelper = new UserHelper();
		questionHelper = new QuestionHelper();

		final ActionBarActivity parent = (ActionBarActivity) getActivity();
		global = (Global) parent.getApplication();

		MODE = parent.getIntent().getExtras().getInt("MODE");
        UID = parent.getIntent().getExtras().getInt("UID");

		answers.add(new ExAnswer("loading..."));

		String questionTitle;
		String questionContent;
		int questionWatchNum;

		if (MODE == QuestionItemActivity.FROM_QUESTION_LIST) {
			QID = parent.getIntent().getExtras().getInt("qid");
			Log.d("q item", String.valueOf(QID));
			questionTitle = parent.getIntent().getExtras().getString("title");
			questionContent = parent.getIntent().getExtras().getString("content");
			questionWatchNum = parent.getIntent().getExtras().getInt("watchNum");
			answerHeader = new AnswerHeader(questionTitle,
					questionContent, questionWatchNum, QID);
		}

		mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.question_answer_recyclerview);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(parent));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(true);
		if (MODE == QuestionItemActivity.FROM_QUESTION_LIST) {
			answerAdapter = new AnswerAdapter(parent, answers, answerHeader);
			answerAdapter.setOnItemClickListener(new AnswerAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					Intent aItemIntent = new Intent(getActivity(), AnswerItemActivity.class);
					ExAnswer exAnswer = answers.get(position - 1);
					aItemIntent.putExtra("aid", exAnswer.getId());
					aItemIntent.putExtra("uid", exAnswer.getUid());
					aItemIntent.putExtra("qid", exAnswer.getQid());
					aItemIntent.putExtra("nickname", exAnswer.getNickname());
					aItemIntent.putExtra("portrait", exAnswer.getUserHead());
					aItemIntent.putExtra("content", exAnswer.getContent());
					aItemIntent.putExtra("approvNum", exAnswer.getScore1());
					aItemIntent.putExtra("title", parent.getIntent().getExtras().getString("title"));
					startActivity(aItemIntent);
				}
			});
			mRecyclerView.setAdapter(answerAdapter);

		} else {
			simpleAnswerAdapter = new SimpleAnswerAdapter(parent, answers);
			simpleAnswerAdapter.setOnItemClickListener(new SimpleAnswerAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					final Intent aItemIntent = new Intent(getActivity(), AnswerItemActivity.class);
					final ExAnswer exAnswer = answers.get(position);
					aItemIntent.putExtra("aid", answers.get(position).getId());
					aItemIntent.putExtra("uid", exAnswer.getUid());
					aItemIntent.putExtra("qid", exAnswer.getQid());
					aItemIntent.putExtra("nickname", exAnswer.getNickname());
					aItemIntent.putExtra("portrait", exAnswer.getUserHead());
					aItemIntent.putExtra("content", exAnswer.getContent());
					aItemIntent.putExtra("approvNum", exAnswer.getScore1());
					//TODO:title传递问题
					startActivity(aItemIntent);
				}
			});
			mRecyclerView.setAdapter(simpleAnswerAdapter);
		}

		initLoadAndRefresh(parent);

		//(new LoadAnswerListTask()).execute(MODE, QID);
	}

	@Override
	public void onResume() {
		super.onResume();
		(new LoadAnswerListTask()).execute(MODE, QID);
	}

	private void initLoadAndRefresh(ActionBarActivity parent) {
		mSwipeLayout = (SwipeRefreshAndLoadLayout) parent.findViewById(R.id.question_item_container);
		mSwipeLayout.setOnRefreshListener(new SwipeRefreshAndLoadLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				(new LoadAnswerListTask()).execute(MODE, QID);
			}

			@Override
			public void onLoadMore() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mSwipeLayout.setRefreshing(false);
						//load more
					}
				}, 1000);
			}
		});
		mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mSwipeLayout.setmMode(SwipeRefreshAndLoadLayout.Mode.BOTH);
	}

	private void setToolbarTitle(int n, ActionBarActivity parent) {
		parent.getSupportActionBar().setTitle("共有" + String.valueOf(n) + "个回答");
	}

	private class LoadAnswerListTask extends AsyncTask<Integer, Integer, Integer> {

        private int focusedFlag = 0;
        private FavoriteHelper favoriteHelper = new FavoriteHelper();
        private ButtonFlat favButton;

		@Override
		protected void onPreExecute() {
			mSwipeLayout.setRefreshing(true);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			int mode = params[0];
			int qid;
            if (mode == QuestionItemActivity.FROM_QUESTION_LIST) {
				qid = params[1];
                focusedFlag = favoriteHelper.isQuestionFavorite(global.getUid(), qid);
				answers.clear();
				answers.addAll(answerHelper.getByQid(qid));
				ExQuestion exQuestion = questionHelper.getByQid(qid);
				answerHeader.setDescription(exQuestion.getContent());
				answerHeader.setTitle(exQuestion.getTitle());
				answerHeader.setWatchNum(exQuestion.getScore1());
				return 0;
			} else {
				answers.clear();
				answers.addAll(userHelper.getAnswers(UID));
				if (answers.size() > 0) {
					return 1;
				} else {
					return -1;
				}
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
				case 1:
					simpleAnswerAdapter.notifyDataSetChanged();
					break;
				case 0:
					answerAdapter.notifyDataSetChanged();
					break;
				case -1:
					break;
			}
			try {
                if (MODE == QuestionItemActivity.FROM_USER_ASKED_LIST) {
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("做过的回答");
                } else {
                    setToolbarTitle(answers.size(), (ActionBarActivity) getActivity());
                }
                favButton = (ButtonFlat) getActivity().findViewById(R.id.question_item_btn_watch);
            } catch (Exception e) {
				//有时在未获取完所有的answer前退出会触发异常，因为先调用了answers.clear()
				//如果这样的现象发生就不改变title
                e.printStackTrace();
			}

            if (MODE == QuestionItemActivity.FROM_QUESTION_LIST) {
                if (global.getLoginFlag()) {
                    //if is logged in
                    favButton.setEnabled(true);
                } else {
                    //if is not logged in, then disable this button
                    favButton.setEnabled(false);
                }
                if (focusedFlag == 1) {
                    favButton.setText("取消关注");
                } else {
                    favButton.setText("关注");
                }
            }
            mSwipeLayout.setRefreshing(false);
		}
	}
}
