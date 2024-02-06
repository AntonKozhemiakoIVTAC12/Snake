package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private final List<SnakePoints> snakePointsList = new ArrayList<>();
    private SurfaceView surfaceView;
    private TextView scoreTV;
    Button likebutton2;
    private SurfaceHolder surfaceHolder;
    // установка дефолтного движения для змеи
    private String movingPosition = "right";
    //рекорд
    private int score = 0;
    // установка размера точки и размера змеи(можно изменять для увеличения или уменьшения)
    private static final int pointSize = 28;
    //дефолтный размер хвоста змеи
    private static final int defaultTalePoints = 3;
    //цвет змеи
    private static final int snakeColor = Color.YELLOW;
    //установка скорости змеи в диапозоне от 1 до 1000
    private static final int snakeMovingSpeed = 800;
    //рандомная  позиция координат точки на SurfaceView
    private  int positionX = 0, positionY = 0;
    //установка таймера для движения змеи/изменение позиции змеи после особого времени
    private Timer timer;
    private Canvas canvas = null;
    private Paint pointColor = null;
    private boolean gameRunning = true;
    private float inGameTime = 0.0f;
    private long startGameTime;
    private long endGameTime;
    Date playdate;
    Date startdate;
    SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm:ss");
    /*DbClass dbClass;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*dbClass = new DbClass(this);*/
        //получение surfaceview и score textview из xml
        surfaceView = findViewById(R.id.surfaceView);
        scoreTV = findViewById(R.id.scoreTV);
        //получение кнопки изображения из xml
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton downBtn = findViewById(R.id.downBtn);
        //добавление обратного вызова для surfaceView
        surfaceView.getHolder().addCallback(this);


        likebutton2 = findViewById(R.id.likebutton2);
        likebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( SecondActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //проверка если предыдущее положение не нижнее, то движение не возможно
                //то есть если она движется вниз она не сможетсразу двигаться наверх
                //змея должна перейти в положение правое или левое перед движением вверх
                if(!movingPosition.equals("down")){
                    movingPosition="top";
                }
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("top")){
                    movingPosition="down";
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("left")){
                    movingPosition="right";
                }
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movingPosition.equals("right")){
                    movingPosition="left";
                }
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //когда поверхность создана, извлекаем из нее SurfaceHolder и назначаем SurfaceHolder
        this.surfaceHolder = surfaceHolder;
        //инициализация для змеи
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder){

    }
    /*private  void init2(){
        Intent i = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(i);
    }*/

    private void init(){
        //очистка точек змеи, ее длины
        snakePointsList.clear();
        //устанвка дефолтногозначения на 0
        scoreTV.setText("0");
        //делает рекорд 0
        score = 0;
        //устанавливает дефолтную позицию движения
        movingPosition = "right";
        //дефолтная позиция стартанаэкране
        int startPositionX = (pointSize) * defaultTalePoints;
        //делает дефолтную длину
        for(int i = 0; i < defaultTalePoints;i++){
            //добавление точек для хвоста змеи
            SnakePoints snakePoints = new SnakePoints(startPositionX,pointSize);
            snakePointsList.add(snakePoints);
            //увеличивание значение для следующей точки по мере для хвоста змеи
            startPositionX = startPositionX - (pointSize * 2);
        }
        //добавление рандомной точки на экран для змеи
        addPoint();
        //начало движения змеи, начало игры
        moveSnake();
    }
    private void addPoint(){
        //получение ширины и высоты surfaceView для добавления точки на поверхности, чтобы быть съеденным змеей
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize *2 );

        int randomXPosition = new Random().nextInt(surfaceWidth/pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight/pointSize);
        //проверка если рандомная X является четным или нечетным значением, нам нужно только четное число
        if((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }
        if((randomYPosition % 2) != 0){
            randomYPosition = randomYPosition + 1;
        }
        positionX = (pointSize*randomXPosition)+pointSize;
        positionY = (pointSize*randomYPosition)+pointSize;
    }
    private void control() {
        try {
            Thread.sleep(27);
            inGameTime += 0.027;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private  void moveSnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //получение позиции змеи
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();
                //проверка на поедание точки
                if(headPositionX == positionX && positionY == headPositionY){
                    //рост змеи после поедания точки
                    growSnake();
                    //добавление следующей рандомной точки на экране
                    addPoint();
                }
                //проверка, в какую сторону движется змея
                switch (movingPosition){
                    case "right":
                        //переместите голову змеи вправо
                        //другие точки следуют за точкой головы змеи, чтобы переместить змею
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize*2));
                        snakePointsList.get(0).setPositionY(headPositionY) ;
                        break;
                    case "left":
                        //переместите голову змеи влево
                        //другие точки следуют за точкой головы змеи, чтобы переместить змею
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize*2));
                        snakePointsList.get(0).setPositionY(headPositionY) ;
                        break;
                    case "top":
                        //переместите голову змеи вверх
                        //другие точки следуют за точкой головы змеи, чтобы переместить змею
                        snakePointsList.get(0).setPositionX(headPositionX) ;
                        snakePointsList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;
                    case "down":
                        //переместите голову змеи вниз
                        //другие точки следуют за точкой головы змеи, чтобы переместить змею
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }
                //проверка если конец игры, когда змея касается краев или самой змеи
                if(checkGameOver(headPositionX, headPositionY)){
                    timer.purge();
                    timer.cancel();
                    //показ окна с проигрышем
                    AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                    builder.setMessage("Your Score = " +score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //перезапуск игры
                            init();
                        }
                    });

                    builder.setNegativeButton("END", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startGameTime = System.currentTimeMillis();
                            gameRunning = false;
                            endGameTime = System.currentTimeMillis();
                            Intent j = new Intent( SecondActivity.this, ResultActivityh.class);
                            j.putExtra("startGameTime", startGameTime);
                            j.putExtra("endGameTime", endGameTime);
                            j.putExtra("inGameTime", score );
                            startActivity(j);
                        }
                    });
                    //таймер работает в фоновом режиме, поэтому нам нужно показать диалоговое окно в основном потоке
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }
                else{
                    //зафиксируйте canvas на SurfaceHolder, чтобы рисовать на нем
                    canvas = surfaceHolder.lockCanvas();
                    //очищение канвас с белым цветом
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    //измените положение головы змеи другие точки змеи будут следовать за головой змеи
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());
                    //нарисуйте случайный точечный круг на поверхности, который будет съеден змеей
                    canvas.drawCircle(positionX,positionY,pointSize, createPointColor());
                    //другие пункты следуют за головой змеи
                    for (int i = 1; i < snakePointsList.size(); i++){
                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();
                        //переместите точки по всей голове
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(),snakePointsList.get(i).getPositionY(),pointSize,createPointColor());
                        //изменение позиции головы
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }
                    //разблокируйте холст для рисования на surfaceview
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        },1000 -snakeMovingSpeed , 1000 - snakeMovingSpeed );

    }
    private void growSnake(){
        //создание новой точки змеи
        SnakePoints snakePoints = new SnakePoints(0, 0);
        // добавление точки для хвоста змеи
        snakePointsList.add(snakePoints);
        //увеличение рекорда
        score++;
        //настройки рекорда для TextView
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTV.setText(String.valueOf(score));
            }
        });
    }
    private boolean checkGameOver(int headPositionX, int headPositionY){
        /*SQLiteDatabase database = dbClass.getWritableDatabase();*/
        boolean gameOver = false;
        //проверка если голова змеи касается границ
        if(snakePointsList.get(0).getPositionX()< 0 ||
                snakePointsList.get(0).getPositionY()< 0||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight())
        {
            gameOver = true;
        }

        else{
            //проверка на то касается ли змея своего хвоста
            for(int i = 1; i < snakePointsList.size(); i++){
                if(headPositionX == snakePointsList.get(i).getPositionX() &&
                        headPositionY == snakePointsList.get(i).getPositionY()){
                    gameOver = true;
                    break;
                }
            }
        }
        return gameOver;
    }
    private Paint createPointColor(){
        //проверка если цвет не определенн ранее
        if(pointColor == null) {
            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);

        }
        return pointColor;
    }



}