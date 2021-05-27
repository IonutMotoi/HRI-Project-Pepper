package com.example.myapplication;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;

public class MainActivity  extends RobotActivity implements RobotLifecycleCallbacks {

    private static final String TAG ="MainActivity";

    // The QiContext provided by the QiSDK.
    private QiContext qiContext = null;

    // Store the Chat action.
    private Chat chatAction;

    // Buttons
    private Button startOrderButton;
    private ImageButton backButton;

    // Menu buttons
    private ImageButton mainsButton;
    private ImageButton sidesButton;
    private ImageButton beveragesButton;
    private ImageButton dessertsButton;

    // Buttons plus and minus (12)
    private ImageButton plusButton1;
    private ImageButton plusButton2;
    private ImageButton plusButton3;
    private ImageButton plusButton4;
    private ImageButton plusButton5;
    private ImageButton plusButton6;
    private ImageButton minusButton1;
    private ImageButton minusButton2;
    private ImageButton minusButton3;
    private ImageButton minusButton4;
    private ImageButton minusButton5;
    private ImageButton minusButton6;

    // Text views
    private TextView counter1;
    private TextView counter2;
    private TextView counter3;
    private TextView counter4;
    private TextView counter5;
    private TextView counter6;

    //Order
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStartView();

        order = new Order();

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Store the provided QiContext.
        this.qiContext = qiContext;
        initActions();

        // Run the Chat action asynchronously.
        Future<Void> chatFuture = chatAction.async().run();

        // Add a lambda to the action execution.
        chatFuture.thenConsume(future -> {
            if (future.hasError()) {
                Log.e(TAG, "Discussion finished with error.", future.getError());
            }
        });
    }

    @Override
    public void onRobotFocusLost() {
        // Remove on started listeners from the Chat action.
        if (chatAction != null) {
            chatAction.removeAllOnStartedListeners();
        }
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    public void initActions() {
        // Create a chat topic
        Topic topic = TopicBuilder.with(qiContext) // Create the builder using the QiContext.
                .withResource(R.raw.greetings) // Set the topic resource.
                .build(); // Build the topic.

        // Create a new QiChatbot.
        QiChatbot qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .build();

        // Create a new Chat action.
        chatAction = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                .build();

        // Add an on started listener to the Chat action.
        chatAction.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));

        // Set up a listener for a chat variable
        QiChatVariable startVariable = qiChatbot.variable("Start");

        startVariable.addOnValueChangedListener(
                currentValue -> {
                    if (currentValue.equals("true")) {
                        Log.i(TAG, "Chat var Start: " + currentValue);
                        initMenuView();
                    }
                }
        );
    }

    public void initStartView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);
                startOrderButton = (Button) findViewById(R.id.button_start_order);
                startOrderButton.setOnClickListener(v -> initMenuView());
            }
        });
    }

    /** Called when the user wants to start the order */
    public void initMenuView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.menu);

                mainsButton = (ImageButton) findViewById(R.id.imagebutton_mains);
                mainsButton.setOnClickListener(v -> initMainsView());

                sidesButton = (ImageButton) findViewById(R.id.imagebutton_sides);
                sidesButton.setOnClickListener(v -> initSidesView());

                beveragesButton = (ImageButton) findViewById(R.id.imagebutton_beverages);
                beveragesButton.setOnClickListener(v -> initBeveragesView());

                dessertsButton = (ImageButton) findViewById(R.id.imagebutton_desserts);
                dessertsButton.setOnClickListener(v -> initDessertsView());
            }
        });
    }

    /** Called when the user wants to start the order */
    public void initMainsView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.mains);

                backButton = (ImageButton) findViewById(R.id.imagebutton_back_mains);
                backButton.setOnClickListener(v -> initMenuView());

                //Hamburger button
                counter1 = (TextView) findViewById(R.id.text_counter_hamburger);

                plusButton1 = (ImageButton) findViewById(R.id.imagebutton_hamburger_plus);
                plusButton1.setOnClickListener(v -> {
                    order.hamburger.number+=1;
                    counter1.setText(order.hamburger.num2str());
                });

                minusButton1 = (ImageButton) findViewById(R.id.imagebutton_hamburger_minus);
                minusButton1.setOnClickListener(v -> {
                    if(order.hamburger.number>0){
                        order.hamburger.number-=1;
                        counter1.setText(order.hamburger.num2str());
                    }
                });
            }
        });
    }

    /** Called when the user wants to start the order */
    public void initSidesView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.sides);

                backButton = (ImageButton) findViewById(R.id.imagebutton_back_sides);
                backButton.setOnClickListener(v -> initMenuView());
            }
        });
    }

    /** Called when the user wants to start the order */
    public void initBeveragesView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.beverages);

                backButton = (ImageButton) findViewById(R.id.imagebutton_back_beverages);
                backButton.setOnClickListener(v -> initMenuView());
            }
        });
    }

    /** Called when the user wants to start the order */
    public void initDessertsView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.desserts);

                backButton = (ImageButton) findViewById(R.id.imagebutton_back_desserts);
                backButton.setOnClickListener(v -> initMenuView());
            }
        });
    }
}