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

                //Set counters
                counter1 = (TextView) findViewById(R.id.text_counter_hamburger);
                counter1.setText(order.hamburger.num2str());
                counter2 = (TextView) findViewById(R.id.text_counter_taco);
                counter2.setText(order.taco.num2str());
                counter3 = (TextView) findViewById(R.id.text_counter_wrap);
                counter3.setText(order.wrap.num2str());
                counter4 = (TextView) findViewById(R.id.text_counter_chicken);
                counter4.setText(order.chicken.num2str());
                counter5 = (TextView) findViewById(R.id.text_counter_toast);
                counter5.setText(order.toast.num2str());
                counter6 = (TextView) findViewById(R.id.text_counter_pizza);
                counter6.setText(order.pizza.num2str());

                //Hamburger buttons
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

                //Taco buttons
                plusButton2 = (ImageButton) findViewById(R.id.imagebutton_taco_plus);
                plusButton2.setOnClickListener(v -> {
                    order.taco.number+=1;
                    counter2.setText(order.taco.num2str());
                });
                minusButton2 = (ImageButton) findViewById(R.id.imagebutton_taco_minus);
                minusButton2.setOnClickListener(v -> {
                    if(order.taco.number>0){
                        order.taco.number-=1;
                        counter2.setText(order.taco.num2str());
                    }
                });

                //Wrap buttons
                plusButton3 = (ImageButton) findViewById(R.id.imagebutton_wrap_plus);
                plusButton3.setOnClickListener(v -> {
                    order.wrap.number+=1;
                    counter3.setText(order.wrap.num2str());
                });
                minusButton3 = (ImageButton) findViewById(R.id.imagebutton_wrap_minus);
                minusButton3.setOnClickListener(v -> {
                    if(order.wrap.number>0){
                        order.wrap.number-=1;
                        counter3.setText(order.wrap.num2str());
                    }
                });

                //Chicken buttons
                plusButton4 = (ImageButton) findViewById(R.id.imagebutton_chicken_plus);
                plusButton4.setOnClickListener(v -> {
                    order.chicken.number+=1;
                    counter4.setText(order.chicken.num2str());
                });
                minusButton4 = (ImageButton) findViewById(R.id.imagebutton_chicken_minus);
                minusButton4.setOnClickListener(v -> {
                    if(order.chicken.number>0){
                        order.chicken.number-=1;
                        counter4.setText(order.chicken.num2str());
                    }
                });

                //Toast buttons
                plusButton5 = (ImageButton) findViewById(R.id.imagebutton_toast_plus);
                plusButton5.setOnClickListener(v -> {
                    order.toast.number+=1;
                    counter5.setText(order.toast.num2str());
                });
                minusButton5 = (ImageButton) findViewById(R.id.imagebutton_toast_minus);
                minusButton5.setOnClickListener(v -> {
                    if(order.toast.number>0){
                        order.toast.number-=1;
                        counter5.setText(order.toast.num2str());
                    }
                });

                //Pizza buttons
                plusButton6 = (ImageButton) findViewById(R.id.imagebutton_pizza_plus);
                plusButton6.setOnClickListener(v -> {
                    order.pizza.number+=1;
                    counter6.setText(order.pizza.num2str());
                });
                minusButton6 = (ImageButton) findViewById(R.id.imagebutton_pizza_minus);
                minusButton6.setOnClickListener(v -> {
                    if(order.pizza.number>0){
                        order.pizza.number-=1;
                        counter6.setText(order.pizza.num2str());
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

                //Set counters
                counter1 = (TextView) findViewById(R.id.text_counter_fries);
                counter1.setText(order.fries.num2str());
                counter2 = (TextView) findViewById(R.id.text_counter_onion);
                counter2.setText(order.onion.num2str());
                counter3 = (TextView) findViewById(R.id.text_counter_sticks);
                counter3.setText(order.sticks.num2str());
                counter4 = (TextView) findViewById(R.id.text_counter_nuggets);
                counter4.setText(order.nuggets.num2str());
                counter5 = (TextView) findViewById(R.id.text_counter_wings);
                counter5.setText(order.wings.num2str());
                counter6 = (TextView) findViewById(R.id.text_counter_salad);
                counter6.setText(order.salad.num2str());

                //Fries buttons
                plusButton1 = (ImageButton) findViewById(R.id.imagebutton_fries_plus);
                plusButton1.setOnClickListener(v -> {
                    order.fries.number+=1;
                    counter1.setText(order.fries.num2str());
                });
                minusButton1 = (ImageButton) findViewById(R.id.imagebutton_fries_minus);
                minusButton1.setOnClickListener(v -> {
                    if(order.fries.number>0){
                        order.fries.number-=1;
                        counter1.setText(order.fries.num2str());
                    }
                });

                //Onion buttons
                plusButton2 = (ImageButton) findViewById(R.id.imagebutton_onion_plus);
                plusButton2.setOnClickListener(v -> {
                    order.onion.number+=1;
                    counter2.setText(order.onion.num2str());
                });
                minusButton2 = (ImageButton) findViewById(R.id.imagebutton_onion_minus);
                minusButton2.setOnClickListener(v -> {
                    if(order.onion.number>0){
                        order.onion.number-=1;
                        counter2.setText(order.onion.num2str());
                    }
                });

                //Sticks buttons
                plusButton3 = (ImageButton) findViewById(R.id.imagebutton_sticks_plus);
                plusButton3.setOnClickListener(v -> {
                    order.sticks.number+=1;
                    counter3.setText(order.sticks.num2str());
                });
                minusButton3 = (ImageButton) findViewById(R.id.imagebutton_sticks_minus);
                minusButton3.setOnClickListener(v -> {
                    if(order.sticks.number>0){
                        order.sticks.number-=1;
                        counter3.setText(order.sticks.num2str());
                    }
                });

                //Nuggets buttons
                plusButton4 = (ImageButton) findViewById(R.id.imagebutton_nuggets_plus);
                plusButton4.setOnClickListener(v -> {
                    order.nuggets.number+=1;
                    counter4.setText(order.nuggets.num2str());
                });
                minusButton4 = (ImageButton) findViewById(R.id.imagebutton_nuggets_minus);
                minusButton4.setOnClickListener(v -> {
                    if(order.nuggets.number>0){
                        order.nuggets.number-=1;
                        counter4.setText(order.nuggets.num2str());
                    }
                });

                //Wings buttons
                plusButton5 = (ImageButton) findViewById(R.id.imagebutton_wings_plus);
                plusButton5.setOnClickListener(v -> {
                    order.wings.number+=1;
                    counter5.setText(order.wings.num2str());
                });
                minusButton5 = (ImageButton) findViewById(R.id.imagebutton_wings_minus);
                minusButton5.setOnClickListener(v -> {
                    if(order.wings.number>0){
                        order.wings.number-=1;
                        counter5.setText(order.wings.num2str());
                    }
                });

                //salad buttons
                plusButton6 = (ImageButton) findViewById(R.id.imagebutton_salad_plus);
                plusButton6.setOnClickListener(v -> {
                    order.salad.number+=1;
                    counter6.setText(order.salad.num2str());
                });
                minusButton6 = (ImageButton) findViewById(R.id.imagebutton_salad_minus);
                minusButton6.setOnClickListener(v -> {
                    if(order.salad.number>0){
                        order.salad.number-=1;
                        counter6.setText(order.salad.num2str());
                    }
                });
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

                //Set counters
                counter1 = (TextView) findViewById(R.id.text_counter_coke);
                counter1.setText(order.coke.num2str());
                counter2 = (TextView) findViewById(R.id.text_counter_sprite);
                counter2.setText(order.sprite.num2str());
                counter3 = (TextView) findViewById(R.id.text_counter_water);
                counter3.setText(order.water.num2str());
                counter4 = (TextView) findViewById(R.id.text_counter_fanta);
                counter4.setText(order.fanta.num2str());
                counter5 = (TextView) findViewById(R.id.text_counter_tea);
                counter5.setText(order.tea.num2str());
                counter6 = (TextView) findViewById(R.id.text_counter_beer);
                counter6.setText(order.beer.num2str());

                //Coke buttons
                plusButton1 = (ImageButton) findViewById(R.id.imagebutton_coke_plus);
                plusButton1.setOnClickListener(v -> {
                    order.coke.number+=1;
                    counter1.setText(order.coke.num2str());
                });
                minusButton1 = (ImageButton) findViewById(R.id.imagebutton_coke_minus);
                minusButton1.setOnClickListener(v -> {
                    if(order.coke.number>0){
                        order.coke.number-=1;
                        counter1.setText(order.coke.num2str());
                    }
                });

                //Sprite buttons
                plusButton2 = (ImageButton) findViewById(R.id.imagebutton_sprite_plus);
                plusButton2.setOnClickListener(v -> {
                    order.sprite.number+=1;
                    counter2.setText(order.sprite.num2str());
                });
                minusButton2 = (ImageButton) findViewById(R.id.imagebutton_sprite_minus);
                minusButton2.setOnClickListener(v -> {
                    if(order.sprite.number>0){
                        order.sprite.number-=1;
                        counter2.setText(order.sprite.num2str());
                    }
                });

                //Water buttons
                plusButton3 = (ImageButton) findViewById(R.id.imagebutton_water_plus);
                plusButton3.setOnClickListener(v -> {
                    order.water.number+=1;
                    counter3.setText(order.water.num2str());
                });
                minusButton3 = (ImageButton) findViewById(R.id.imagebutton_water_minus);
                minusButton3.setOnClickListener(v -> {
                    if(order.water.number>0){
                        order.water.number-=1;
                        counter3.setText(order.water.num2str());
                    }
                });

                //Fanta buttons
                plusButton4 = (ImageButton) findViewById(R.id.imagebutton_fanta_plus);
                plusButton4.setOnClickListener(v -> {
                    order.fanta.number+=1;
                    counter4.setText(order.fanta.num2str());
                });
                minusButton4 = (ImageButton) findViewById(R.id.imagebutton_fanta_minus);
                minusButton4.setOnClickListener(v -> {
                    if(order.fanta.number>0){
                        order.fanta.number-=1;
                        counter4.setText(order.fanta.num2str());
                    }
                });

                //Tea buttons
                plusButton5 = (ImageButton) findViewById(R.id.imagebutton_tea_plus);
                plusButton5.setOnClickListener(v -> {
                    order.tea.number+=1;
                    counter5.setText(order.tea.num2str());
                });
                minusButton5 = (ImageButton) findViewById(R.id.imagebutton_tea_minus);
                minusButton5.setOnClickListener(v -> {
                    if(order.tea.number>0){
                        order.tea.number-=1;
                        counter5.setText(order.tea.num2str());
                    }
                });

                //salad buttons
                plusButton6 = (ImageButton) findViewById(R.id.imagebutton_beer_plus);
                plusButton6.setOnClickListener(v -> {
                    order.beer.number+=1;
                    counter6.setText(order.beer.num2str());
                });
                minusButton6 = (ImageButton) findViewById(R.id.imagebutton_beer_minus);
                minusButton6.setOnClickListener(v -> {
                    if(order.beer.number>0){
                        order.beer.number-=1;
                        counter6.setText(order.beer.num2str());
                    }
                });
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

                //Set counters
                counter1 = (TextView) findViewById(R.id.text_counter_cake);
                counter1.setText(order.cake.num2str());
                counter2 = (TextView) findViewById(R.id.text_counter_donut);
                counter2.setText(order.donut.num2str());
                counter3 = (TextView) findViewById(R.id.text_counter_milkshake);
                counter3.setText(order.milkshake.num2str());
                counter4 = (TextView) findViewById(R.id.text_counter_crepes);
                counter4.setText(order.crepes.num2str());
                counter5 = (TextView) findViewById(R.id.text_counter_icecream);
                counter5.setText(order.icecream.num2str());
                counter6 = (TextView) findViewById(R.id.text_counter_pancake);
                counter6.setText(order.pancake.num2str());

                //Cake buttons
                plusButton1 = (ImageButton) findViewById(R.id.imagebutton_cake_plus);
                plusButton1.setOnClickListener(v -> {
                    order.cake.number+=1;
                    counter1.setText(order.cake.num2str());
                });
                minusButton1 = (ImageButton) findViewById(R.id.imagebutton_cake_minus);
                minusButton1.setOnClickListener(v -> {
                    if(order.cake.number>0){
                        order.cake.number-=1;
                        counter1.setText(order.cake.num2str());
                    }
                });

                //Donuts buttons
                plusButton2 = (ImageButton) findViewById(R.id.imagebutton_donut_plus);
                plusButton2.setOnClickListener(v -> {
                    order.donut.number+=1;
                    counter2.setText(order.donut.num2str());
                });
                minusButton2 = (ImageButton) findViewById(R.id.imagebutton_donut_minus);
                minusButton2.setOnClickListener(v -> {
                    if(order.donut.number>0){
                        order.donut.number-=1;
                        counter2.setText(order.donut.num2str());
                    }
                });

                //Milkshake buttons
                plusButton3 = (ImageButton) findViewById(R.id.imagebutton_milkshake_plus);
                plusButton3.setOnClickListener(v -> {
                    order.milkshake.number+=1;
                    counter3.setText(order.milkshake.num2str());
                });
                minusButton3 = (ImageButton) findViewById(R.id.imagebutton_milkshake_minus);
                minusButton3.setOnClickListener(v -> {
                    if(order.milkshake.number>0){
                        order.milkshake.number-=1;
                        counter3.setText(order.milkshake.num2str());
                    }
                });

                //Crepes buttons
                plusButton4 = (ImageButton) findViewById(R.id.imagebutton_crepes_plus);
                plusButton4.setOnClickListener(v -> {
                    order.crepes.number+=1;
                    counter4.setText(order.crepes.num2str());
                });
                minusButton4 = (ImageButton) findViewById(R.id.imagebutton_crepes_minus);
                minusButton4.setOnClickListener(v -> {
                    if(order.crepes.number>0){
                        order.crepes.number-=1;
                        counter4.setText(order.crepes.num2str());
                    }
                });

                //Ice Cream buttons
                plusButton5 = (ImageButton) findViewById(R.id.imagebutton_icecream_plus);
                plusButton5.setOnClickListener(v -> {
                    order.icecream.number+=1;
                    counter5.setText(order.icecream.num2str());
                });
                minusButton5 = (ImageButton) findViewById(R.id.imagebutton_icecream_minus);
                minusButton5.setOnClickListener(v -> {
                    if(order.icecream.number>0){
                        order.icecream.number-=1;
                        counter5.setText(order.icecream.num2str());
                    }
                });

                //Pancake buttons
                plusButton6 = (ImageButton) findViewById(R.id.imagebutton_pancake_plus);
                plusButton6.setOnClickListener(v -> {
                    order.pancake.number+=1;
                    counter6.setText(order.pancake.num2str());
                });
                minusButton6 = (ImageButton) findViewById(R.id.imagebutton_pancake_minus);
                minusButton6.setOnClickListener(v -> {
                    if(order.pancake.number>0){
                        order.pancake.number-=1;
                        counter6.setText(order.pancake.num2str());
                    }
                });
            }
        });
    }
}