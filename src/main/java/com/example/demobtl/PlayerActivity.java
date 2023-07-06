package com.example.demobtl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    //Khai báo các TextView
    private TextView tvQuestion;
    private TextView tvLevel;
    private TextView tvCaseA;
    private TextView tvCaseB;
    private TextView tvCaseC;
    private TextView tvCaseD;

    private List<Question> mListQuestion;

    private Question mQuestion;
    private int currentQuestion = 0;

    private int currentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Lấy id từ layout
        tvQuestion = findViewById(R.id.tv_question);
        tvLevel = findViewById(R.id.tv_level);
        tvCaseA = findViewById(R.id.tv_case_a);
        tvCaseB = findViewById(R.id.tv_case_b);
        tvCaseC = findViewById(R.id.tv_case_c);
        tvCaseD = findViewById(R.id.tv_case_d);

        //Đăng ký sự kiện onClick để có các tùy chọn trả lời TextViews
        tvCaseA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(tvCaseA, mQuestion, mQuestion.getListAnswer().get(0));
            }
        });
        tvCaseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(tvCaseB, mQuestion, mQuestion.getListAnswer().get(1));
            }
        });
        tvCaseC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(tvCaseC, mQuestion, mQuestion.getListAnswer().get(2));
            }
        });
        tvCaseD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(tvCaseD, mQuestion, mQuestion.getListAnswer().get(3));
            }
        });


        mListQuestion = getListQuestion();
        if (mListQuestion.isEmpty()) {
            return;
        }
        setDataQuestion(mListQuestion.get(currentQuestion));
    }

    //set đata cho app
    private void setDataQuestion(Question question) {

        if (question == null) {
            return;
        }
        mQuestion = question;

        String tittleQuesttion = "Question " + question.getNumber();

        tvLevel.setText(tittleQuesttion);
        tvQuestion.setText(question.getContent());
        tvCaseA.setText(question.getListAnswer().get(0).getContent());
        tvCaseB.setText(question.getListAnswer().get(1).getContent());
        tvCaseC.setText(question.getListAnswer().get(2).getContent());
        tvCaseD.setText(question.getListAnswer().get(3).getContent());

    }

    // Tạo câu hỏi + đáp án và xử lý logic đúng sai cho đáp án
    private List<Question> getListQuestion() {
        List<Question> list = new ArrayList<>();

        //Đáp án câu hỏi 1
        List<Answer> answerList1 = new ArrayList<>();
        answerList1.add(new Answer("Cái xe máy", false));
        answerList1.add(new Answer("Cái chổi", false));
        answerList1.add(new Answer("Đám mây", false));
        answerList1.add(new Answer("Cái bóng", true));

        //Đáp án câu hỏi 2
        List<Answer> answerList2 = new ArrayList<>();
        answerList2.add(new Answer("Thông minh", true));
        answerList2.add(new Answer("Chăm chỉ", false));
        answerList2.add(new Answer("Siêng năng", false));
        answerList2.add(new Answer("Kĩ năng", false));

        //Đáp án câu hỏi 3
        List<Answer> answerList3 = new ArrayList<>();
        answerList3.add(new Answer("Tiền tệ ", false));
        answerList3.add(new Answer("Tiền đồ", false));
        answerList3.add(new Answer("Tiền án, tiền sự", true));
        answerList3.add(new Answer("Tiền vệ ", false));

        // List câu hỏi
        list.add(new Question("Nắng ba năm tôi không bỏ bạn, mưa 1 ngày sao bạn lại bỏ tôi là cái gì?", 1, answerList1));
        list.add(new Question("Cần cù thì bù ... ",2, answerList2));
        list.add(new Question("Tiền gì mọi người không thích?",3, answerList3));

        return list;
    }

    // Xử lý logic Check Answer
    private void checkAnswer(TextView textView, Question question, Answer answer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Nếu đúng thì đến câu hỏi tiếp theo
                if (answer.isCorrect()) {
                    nextQuestion();
                }

                //Nếu sai thì hiện đáp án đúng và thông báo Game Over
                else {
                    gameOver();
                    showCorrectAnswer(question);
                }
            }

        }, 1000);
    }

    //Hiển thị thông báo Game Over
    private void gameOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog("Game Over");
            }
        },1000);
    }

    //Hiển thị đáp án đúng cho câu hỏi vừa trả lười sai
    private void showCorrectAnswer(Question question) {
        String correctAnswer = getCorrectAnswer(question);
        showToast("Đáp án đúng là: " + correctAnswer);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getCorrectAnswer(Question question) {
        List<Answer> answerList = question.getListAnswer();
        for (Answer answer : answerList) {
            if (answer.isCorrect()) {
                return answer.getContent();
            }
        }
        return ""; // Trả về chuỗi rỗng nếu không tìm thấy đáp án đúng
    }

    //Hiển thị câu hỏi kế tiếp
    private void nextQuestion() {
        if (currentQuestion == mListQuestion.size() - 1) {
            showDialog("Chúc mừng bạn. Bạn đã hoàn thành tất cả câu hỏi <3");
        } else {
            currentQuestion++;
            setDataQuestion(mListQuestion.get(currentQuestion));
        }
    }

    //Hiển thị thông báo
    private void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder( this);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Kết thúc PlayerActivity
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

