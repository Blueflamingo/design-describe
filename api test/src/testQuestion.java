import java.io.File;
import java.util.ArrayList;

import cn.edu.fudan.blueflamingo.HandinHandServerHelper.Answer;
import cn.edu.fudan.blueflamingo.HandinHandServerHelper.QuestionHelper;
import cn.edu.fudan.blueflamingo.HandinHandServerHelper.Question;
import cn.edu.fudan.blueflamingo.HandinHandServerHelper.User;
import cn.edu.fudan.blueflamingo.HandinHandServerHelper.ExQuestion;

public class testQuestion {
	
	public static void main(String[] args) {
		QuestionHelper helperquestion = new QuestionHelper();
		
		 //helperquestion.uploadFile("demo.jpg");
		
		ArrayList<Integer> topics = new ArrayList<Integer>();
		topics.add(1); 
		topics.add(2);
		//test add
		Question q =new Question(5,"zst",1,2,35,20, "demo.jpg","demo",topics);
		//q.createdTime = System.currentTimeMillis();
		q.createdTime = 0;
		q.expireTime = System.currentTimeMillis() - 2000 * 1000;
		System.out.println(q.createdTime);
		int qid = helperquestion.add(q);
		/*System.out.print(qid);
		ArrayList<Integer> topics1 = new ArrayList<Integer>();
		topics1.add(1); 
		Question q2 = new Question(6,"cat",1,2,35,20, "demo.jpg","demo",topics1);
		int qid2 = helperquestion.add(q2);
		System.out.print(qid2);
		
		//test update
		Question q1 = new Question(5,"world",1,2,35,20, "demo.jpg","demo",topics);
		int num = helperquestion.update(q1);
		System.out.print(num);
		
		//test delete
		int num1 = helperquestion.delete(5);
		System.out.print(num1);
		//test getByTopic
		ArrayList<ExQuestion> ques1 = new ArrayList<ExQuestion>();
		ArrayList<ExQuestion> ques2 = new ArrayList<ExQuestion>();
		ques1 = helperquestion.getByTopic(1);
		ques2 = helperquestion.getByTopic(2);
		System.out.print(ques1);
		System.out.print(ques2);
		
		//test getByQid
		Question q3 = new Question();
		q3 = helperquestion.getByQid(40);
		System.out.print(q3);
		*/
		
		
		
		
		
		
	}
	
}
