package com.example.myapplication;


import android.annotation.SuppressLint;
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
import com.aldebaran.qi.sdk.object.conversation.Bookmark;
import com.aldebaran.qi.sdk.object.conversation.BookmarkStatus;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;

import java.util.Map;

public class MainActivity  extends RobotActivity implements RobotLifecycleCallbacks {

    private static final String TAG ="MainActivity";

    // The QiContext provided by the QiSDK.
    private QiContext qiContext = null;

    // Store the QiChatbot
    private QiChatbot qiChatbot;

    // Store the Chat Action.
    private Chat chatAction;

    // Store the Chat Future
    private Future<Void> chatFuture;

    // Chat variables
    private QiChatVariable foodVariable;
    private QiChatVariable numberVariable;

    // Chat bookmarks
    private BookmarkStatus menuBookmarkStatus;
    private BookmarkStatus addItemBookmarkStatus;

    // Chat topics
    private Topic topicGreetings;
    private Topic topicMenu;

    // Buttons
    private Button startOrderButton;
    private ImageButton backButton;

    // Menu Buttons
    private ImageButton mainsButton;
    private ImageButton sidesButton;
    private ImageButton beveragesButton;
    private ImageButton dessertsButton;
    private Button cancelOrderButton;
    private Button checkoutButton;

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

        // Initialize start view
        initStartView();

        // Initialize chat actions
        initActions();

        // Run the Chat action asynchronously.
        chatFuture = chatAction.async().run();

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

        // Remove the listeners on each BookmarkStatus
        if (menuBookmarkStatus != null) {menuBookmarkStatus.removeAllOnReachedListeners();}
        if (addItemBookmarkStatus != null) {addItemBookmarkStatus.removeAllOnReachedListeners();}
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    public void initActions() {
        // Create chat topics
        topicGreetings = TopicBuilder.with(qiContext) // Create the builder using the QiContext.
                .withResource(R.raw.greetings) // Set the topic resource.
                .build(); // Build the topic.
        topicMenu = TopicBuilder.with(qiContext) // Create the builder using the QiContext.
                .withResource(R.raw.menu) // Set the topic resource.
                .build(); // Build the topic.

        // Create a new QiChatbot
        qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topicGreetings, topicMenu)
                .build();

        // Create a new Chat action
        chatAction = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                .build();

        // Add an on started listener to the Chat action.
        chatAction.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));

        initChatVariables();
        initBookmarks();
        initAnimations();
    }

    void initChatVariables() {
        // Start
        QiChatVariable startVariable = qiChatbot.variable("Start");
        startVariable.addOnValueChangedListener(
                currentValue -> {
                    if (currentValue.equals("true")) {
                        Log.i(TAG, "Chat var Start: " + currentValue);
                        initMenuView();
                    }
                }
        );

        // Food
        foodVariable = qiChatbot.variable("Food");

        // Only for log
        foodVariable.addOnValueChangedListener(
                currentFoodValue -> Log.i(TAG, "Chat var Food: " + currentFoodValue)
        );

        // Number
        numberVariable = qiChatbot.variable("Number");
        numberVariable.addOnValueChangedListener(
                currentNumberValue -> Log.i(TAG, "Chat var Number: " + numberStringToInt(currentNumberValue))
        );
    }

    public void initBookmarks() {
        // Get the bookmarks from the topic
        Map<String, Bookmark> bookmarksGreetings = topicGreetings.getBookmarks();
        Map<String, Bookmark> bookmarksMenu = topicMenu.getBookmarks();

        // Get the bookmarks
        Bookmark menuBookmark = bookmarksGreetings.get("menu");
        Bookmark addItemBookmark = bookmarksMenu.get("additem");

        // Create a BookmarkStatus for each bookmark
        menuBookmarkStatus = qiChatbot.bookmarkStatus(menuBookmark);
        addItemBookmarkStatus = qiChatbot.bookmarkStatus(addItemBookmark);

        menuBookmarkStatus.addOnReachedListener(this::initMenuView);
        addItemBookmarkStatus.addOnReachedListener(() -> {
            String food = foodVariable.getValue();
            int number = numberStringToInt(numberVariable.getValue());

            // Add item/items to order
            if (!order.getFoodItem(food).getName().equals("null")) {
                order.getFoodItem(food).number = number;
            }

            // Go to the View containing the requested food
            initCategoryFromName(food);
        });
    }

    public void initAnimations() {

    }

    public void initStartView() {
        runOnUiThread(() -> {

            setContentView(R.layout.activity_main);

            startOrderButton = findViewById(R.id.button_start_order);
            startOrderButton.setOnClickListener(v -> initMenuView());

            order = new Order();
        });
    }

    /** Called when the user wants to start the order */
    public void initMenuView() {
        runOnUiThread(() -> {
            setContentView(R.layout.menu);

            UpdateOrder();

            cancelOrderButton = findViewById(R.id.button_cancel_order);
            cancelOrderButton.setOnClickListener(v -> restartOrder());

            checkoutButton = findViewById(R.id.button_checkout);
            checkoutButton.setOnClickListener(v -> restartOrder());

            mainsButton = findViewById(R.id.imagebutton_mains);
            mainsButton.setOnClickListener(v -> initMainsView());

            sidesButton = findViewById(R.id.imagebutton_sides);
            sidesButton.setOnClickListener(v -> initSidesView());

            beveragesButton = findViewById(R.id.imagebutton_beverages);
            beveragesButton.setOnClickListener(v -> initBeveragesView());

            dessertsButton = findViewById(R.id.imagebutton_desserts);
            dessertsButton.setOnClickListener(v -> initDessertsView());
        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void UpdateOrder() {
        TextView orderListTextView = findViewById(R.id.text_order_list);
        TextView orderTotalTextView = findViewById(R.id.text_order_total);

        orderListTextView.setText(order.getOrderText());
        orderTotalTextView.setText("Total: " + String.format("%.2f", order.getTotal() / 100.0f) + " €");
    }

    /** Called when the user wants to start the order */
    public void initMainsView() {
        runOnUiThread(() -> {
            setContentView(R.layout.mains);

            backButton = findViewById(R.id.imagebutton_back_mains);
            backButton.setOnClickListener(v -> initMenuView());

            //Set counters
            counter1 = findViewById(R.id.text_counter_hamburger);
            counter1.setText(order.list[0].num2str());
            counter2 = findViewById(R.id.text_counter_taco);
            counter2.setText(order.list[1].num2str());
            counter3 = findViewById(R.id.text_counter_wrap);
            counter3.setText(order.list[2].num2str());
            counter4 = findViewById(R.id.text_counter_chicken);
            counter4.setText(order.list[3].num2str());
            counter5 = findViewById(R.id.text_counter_toast);
            counter5.setText(order.list[4].num2str());
            counter6 = findViewById(R.id.text_counter_pizza);
            counter6.setText(order.list[5].num2str());

            //Hamburger buttons
            plusButton1 = findViewById(R.id.imagebutton_hamburger_plus);
            plusButton1.setOnClickListener(v -> {
                order.list[0].number+=1;
                counter1.setText(order.list[0].num2str());
            });
            minusButton1 = findViewById(R.id.imagebutton_hamburger_minus);
            minusButton1.setOnClickListener(v -> {
                if(order.list[0].number>0){
                    order.list[0].number-=1;
                    counter1.setText(order.list[0].num2str());
                }
            });

            //Taco buttons
            plusButton2 = findViewById(R.id.imagebutton_taco_plus);
            plusButton2.setOnClickListener(v -> {
                order.list[1].number+=1;
                counter2.setText(order.list[1].num2str());
            });
            minusButton2 = findViewById(R.id.imagebutton_taco_minus);
            minusButton2.setOnClickListener(v -> {
                if(order.list[1].number>0){
                    order.list[1].number-=1;
                    counter2.setText(order.list[1].num2str());
                }
            });

            //Wrap buttons
            plusButton3 = findViewById(R.id.imagebutton_wrap_plus);
            plusButton3.setOnClickListener(v -> {
                order.list[2].number+=1;
                counter3.setText(order.list[2].num2str());
            });
            minusButton3 = findViewById(R.id.imagebutton_wrap_minus);
            minusButton3.setOnClickListener(v -> {
                if(order.list[2].number>0){
                    order.list[2].number-=1;
                    counter3.setText(order.list[2].num2str());
                }
            });

            //Chicken buttons
            plusButton4 = findViewById(R.id.imagebutton_chicken_plus);
            plusButton4.setOnClickListener(v -> {
                order.list[3].number+=1;
                counter4.setText(order.list[3].num2str());
            });
            minusButton4 = findViewById(R.id.imagebutton_chicken_minus);
            minusButton4.setOnClickListener(v -> {
                if(order.list[3].number>0){
                    order.list[3].number-=1;
                    counter4.setText(order.list[3].num2str());
                }
            });

            //Toast buttons
            plusButton5 = findViewById(R.id.imagebutton_toast_plus);
            plusButton5.setOnClickListener(v -> {
                order.list[4].number+=1;
                counter5.setText(order.list[4].num2str());
            });
            minusButton5 = findViewById(R.id.imagebutton_toast_minus);
            minusButton5.setOnClickListener(v -> {
                if(order.list[4].number>0){
                    order.list[4].number-=1;
                    counter5.setText(order.list[4].num2str());
                }
            });

            //Pizza buttons
            plusButton6 = findViewById(R.id.imagebutton_pizza_plus);
            plusButton6.setOnClickListener(v -> {
                order.list[5].number+=1;
                counter6.setText(order.list[5].num2str());
            });
            minusButton6 = findViewById(R.id.imagebutton_pizza_minus);
            minusButton6.setOnClickListener(v -> {
                if(order.list[5].number>0){
                    order.list[5].number-=1;
                    counter6.setText(order.list[5].num2str());
                }
            });
        });
    }

    /** Called when the user wants to start the order */
    public void initSidesView() {
        runOnUiThread(() -> {
            setContentView(R.layout.sides);

            backButton = findViewById(R.id.imagebutton_back_sides);
            backButton.setOnClickListener(v -> initMenuView());

            //Set counters
            counter1 = findViewById(R.id.text_counter_fries);
            counter1.setText(order.list[6].num2str());
            counter2 = findViewById(R.id.text_counter_onion);
            counter2.setText(order.list[7].num2str());
            counter3 = findViewById(R.id.text_counter_sticks);
            counter3.setText(order.list[8].num2str());
            counter4 = findViewById(R.id.text_counter_nuggets);
            counter4.setText(order.list[9].num2str());
            counter5 = findViewById(R.id.text_counter_wings);
            counter5.setText(order.list[10].num2str());
            counter6 = findViewById(R.id.text_counter_salad);
            counter6.setText(order.list[11].num2str());

            //Fries buttons
            plusButton1 = findViewById(R.id.imagebutton_fries_plus);
            plusButton1.setOnClickListener(v -> {
                order.list[6].number+=1;
                counter1.setText(order.list[6].num2str());
            });
            minusButton1 = findViewById(R.id.imagebutton_fries_minus);
            minusButton1.setOnClickListener(v -> {
                if(order.list[6].number>0){
                    order.list[6].number-=1;
                    counter1.setText(order.list[6].num2str());
                }
            });

            //Onion buttons
            plusButton2 = findViewById(R.id.imagebutton_onion_plus);
            plusButton2.setOnClickListener(v -> {
                order.list[7].number+=1;
                counter2.setText(order.list[7].num2str());
            });
            minusButton2 = findViewById(R.id.imagebutton_onion_minus);
            minusButton2.setOnClickListener(v -> {
                if(order.list[7].number>0){
                    order.list[7].number-=1;
                    counter2.setText(order.list[7].num2str());
                }
            });

            //Sticks buttons
            plusButton3 = findViewById(R.id.imagebutton_sticks_plus);
            plusButton3.setOnClickListener(v -> {
                order.list[8].number+=1;
                counter3.setText(order.list[8].num2str());
            });
            minusButton3 = findViewById(R.id.imagebutton_sticks_minus);
            minusButton3.setOnClickListener(v -> {
                if(order.list[8].number>0){
                    order.list[8].number-=1;
                    counter3.setText(order.list[8].num2str());
                }
            });

            //Nuggets buttons
            plusButton4 = findViewById(R.id.imagebutton_nuggets_plus);
            plusButton4.setOnClickListener(v -> {
                order.list[9].number+=1;
                counter4.setText(order.list[9].num2str());
            });
            minusButton4 = findViewById(R.id.imagebutton_nuggets_minus);
            minusButton4.setOnClickListener(v -> {
                if(order.list[9].number>0){
                    order.list[9].number-=1;
                    counter4.setText(order.list[9].num2str());
                }
            });

            //Wings buttons
            plusButton5 = findViewById(R.id.imagebutton_wings_plus);
            plusButton5.setOnClickListener(v -> {
                order.list[10].number+=1;
                counter5.setText(order.list[10].num2str());
            });
            minusButton5 = findViewById(R.id.imagebutton_wings_minus);
            minusButton5.setOnClickListener(v -> {
                if(order.list[10].number>0){
                    order.list[10].number-=1;
                    counter5.setText(order.list[10].num2str());
                }
            });

            //salad buttons
            plusButton6 = findViewById(R.id.imagebutton_salad_plus);
            plusButton6.setOnClickListener(v -> {
                order.list[11].number+=1;
                counter6.setText(order.list[11].num2str());
            });
            minusButton6 = findViewById(R.id.imagebutton_salad_minus);
            minusButton6.setOnClickListener(v -> {
                if(order.list[11].number>0){
                    order.list[11].number-=1;
                    counter6.setText(order.list[11].num2str());
                }
            });
        });
    }

    /** Called when the user wants to start the order */
    public void initBeveragesView() {
        runOnUiThread(() -> {
            setContentView(R.layout.beverages);

            backButton = findViewById(R.id.imagebutton_back_beverages);
            backButton.setOnClickListener(v -> initMenuView());

            //Set counters
            counter1 = findViewById(R.id.text_counter_coke);
            counter1.setText(order.list[12].num2str());
            counter2 = findViewById(R.id.text_counter_sprite);
            counter2.setText(order.list[13].num2str());
            counter3 = findViewById(R.id.text_counter_water);
            counter3.setText(order.list[14].num2str());
            counter4 = findViewById(R.id.text_counter_fanta);
            counter4.setText(order.list[15].num2str());
            counter5 = findViewById(R.id.text_counter_tea);
            counter5.setText(order.list[16].num2str());
            counter6 = findViewById(R.id.text_counter_beer);
            counter6.setText(order.list[17].num2str());

            //Coke buttons
            plusButton1 = findViewById(R.id.imagebutton_coke_plus);
            plusButton1.setOnClickListener(v -> {
                order.list[12].number+=1;
                counter1.setText(order.list[12].num2str());
            });
            minusButton1 = findViewById(R.id.imagebutton_coke_minus);
            minusButton1.setOnClickListener(v -> {
                if(order.list[12].number>0){
                    order.list[12].number-=1;
                    counter1.setText(order.list[12].num2str());
                }
            });

            //Sprite buttons
            plusButton2 = findViewById(R.id.imagebutton_sprite_plus);
            plusButton2.setOnClickListener(v -> {
                order.list[13].number+=1;
                counter2.setText(order.list[13].num2str());
            });
            minusButton2 = findViewById(R.id.imagebutton_sprite_minus);
            minusButton2.setOnClickListener(v -> {
                if(order.list[13].number>0){
                    order.list[13].number-=1;
                    counter2.setText(order.list[13].num2str());
                }
            });

            //Water buttons
            plusButton3 = findViewById(R.id.imagebutton_water_plus);
            plusButton3.setOnClickListener(v -> {
                order.list[14].number+=1;
                counter3.setText(order.list[14].num2str());
            });
            minusButton3 = findViewById(R.id.imagebutton_water_minus);
            minusButton3.setOnClickListener(v -> {
                if(order.list[14].number>0){
                    order.list[14].number-=1;
                    counter3.setText(order.list[14].num2str());
                }
            });

            // Fanta buttons
            plusButton4 = findViewById(R.id.imagebutton_fanta_plus);
            plusButton4.setOnClickListener(v -> {
                order.list[15].number+=1;
                counter4.setText(order.list[15].num2str());
            });
            minusButton4 = findViewById(R.id.imagebutton_fanta_minus);
            minusButton4.setOnClickListener(v -> {
                if(order.list[15].number>0){
                    order.list[15].number-=1;
                    counter4.setText(order.list[15].num2str());
                }
            });

            //Tea buttons
            plusButton5 = findViewById(R.id.imagebutton_tea_plus);
            plusButton5.setOnClickListener(v -> {
                order.list[16].number+=1;
                counter5.setText(order.list[16].num2str());
            });
            minusButton5 = findViewById(R.id.imagebutton_tea_minus);
            minusButton5.setOnClickListener(v -> {
                if(order.list[16].number>0){
                    order.list[16].number-=1;
                    counter5.setText(order.list[16].num2str());
                }
            });

            //salad buttons
            plusButton6 = findViewById(R.id.imagebutton_beer_plus);
            plusButton6.setOnClickListener(v -> {
                order.list[17].number+=1;
                counter6.setText(order.list[17].num2str());
            });
            minusButton6 = findViewById(R.id.imagebutton_beer_minus);
            minusButton6.setOnClickListener(v -> {
                if(order.list[17].number>0){
                    order.list[17].number-=1;
                    counter6.setText(order.list[17].num2str());
                }
            });
        });
    }

    /** Called when the user wants to start the order */
    public void initDessertsView() {
        runOnUiThread(() -> {
            setContentView(R.layout.desserts);

            backButton = findViewById(R.id.imagebutton_back_desserts);
            backButton.setOnClickListener(v -> initMenuView());

            //Set counters
            counter1 = findViewById(R.id.text_counter_cake);
            counter1.setText(order.list[18].num2str());
            counter2 = findViewById(R.id.text_counter_donut);
            counter2.setText(order.list[19].num2str());
            counter3 = findViewById(R.id.text_counter_milkshake);
            counter3.setText(order.list[20].num2str());
            counter4 = findViewById(R.id.text_counter_crepes);
            counter4.setText(order.list[21].num2str());
            counter5 = findViewById(R.id.text_counter_icecream);
            counter5.setText(order.list[22].num2str());
            counter6 = findViewById(R.id.text_counter_pancake);
            counter6.setText(order.list[23].num2str());

            //Cake buttons
            plusButton1 = findViewById(R.id.imagebutton_cake_plus);
            plusButton1.setOnClickListener(v -> {
                order.list[18].number+=1;
                counter1.setText(order.list[18].num2str());
            });
            minusButton1 = findViewById(R.id.imagebutton_cake_minus);
            minusButton1.setOnClickListener(v -> {
                if(order.list[18].number>0){
                    order.list[18].number-=1;
                    counter1.setText(order.list[18].num2str());
                }
            });

            //Donuts buttons
            plusButton2 = findViewById(R.id.imagebutton_donut_plus);
            plusButton2.setOnClickListener(v -> {
                order.list[19].number+=1;
                counter2.setText(order.list[19].num2str());
            });
            minusButton2 = findViewById(R.id.imagebutton_donut_minus);
            minusButton2.setOnClickListener(v -> {
                if(order.list[19].number>0){
                    order.list[19].number-=1;
                    counter2.setText(order.list[19].num2str());
                }
            });

            //Milkshake buttons
            plusButton3 = findViewById(R.id.imagebutton_milkshake_plus);
            plusButton3.setOnClickListener(v -> {
                order.list[20].number+=1;
                counter3.setText(order.list[20].num2str());
            });
            minusButton3 = findViewById(R.id.imagebutton_milkshake_minus);
            minusButton3.setOnClickListener(v -> {
                if(order.list[20].number>0){
                    order.list[20].number-=1;
                    counter3.setText(order.list[20].num2str());
                }
            });

            //Crepes buttons
            plusButton4 = findViewById(R.id.imagebutton_crepes_plus);
            plusButton4.setOnClickListener(v -> {
                order.list[21].number+=1;
                counter4.setText(order.list[21].num2str());
            });
            minusButton4 = findViewById(R.id.imagebutton_crepes_minus);
            minusButton4.setOnClickListener(v -> {
                if(order.list[21].number>0){
                    order.list[21].number-=1;
                    counter4.setText(order.list[21].num2str());
                }
            });

            //Ice Cream buttons
            plusButton5 = findViewById(R.id.imagebutton_icecream_plus);
            plusButton5.setOnClickListener(v -> {
                order.list[22].number+=1;
                counter5.setText(order.list[22].num2str());
            });
            minusButton5 = findViewById(R.id.imagebutton_icecream_minus);
            minusButton5.setOnClickListener(v -> {
                if(order.list[22].number>0){
                    order.list[22].number-=1;
                    counter5.setText(order.list[22].num2str());
                }
            });

            //Pancake buttons
            plusButton6 = findViewById(R.id.imagebutton_pancake_plus);
            plusButton6.setOnClickListener(v -> {
                order.list[23].number+=1;
                counter6.setText(order.list[23].num2str());
            });
            minusButton6 = findViewById(R.id.imagebutton_pancake_minus);
            minusButton6.setOnClickListener(v -> {
                if(order.list[23].number>0){
                    order.list[23].number-=1;
                    counter6.setText(order.list[23].num2str());
                }
            });
        });
    }

    public void restartOrder() {
        chatFuture.requestCancellation();
        this.recreate();
    }

    public int numberStringToInt(String str) {
        switch (str) {
            case "one": return 1;
            case "two": return 2;
            case "three": return 3;
            case "four": return 4;
            case "five": return 5;
            case "six": return 6;
            case "seven": return 7;
            case "eight": return 8;
            case "nine": return 9;
            case "ten": return 10;
            default: return 0;
        }
    }

    public void initCategoryFromName(String str) {
        switch (str) {
            case "Hamburger":
            case "Taco":
            case "Wrap":
            case "Chicken":
            case "Toast":
            case "Pizza": {
                initMainsView();
                break;
            }

            case "Fries":
            case "Onion rings":
            case "Mozzarella":
            case "Nuggets":
            case "Chicken wings":
            case "Salad": {
                initSidesView();
                break;
            }

            case "Coke":
            case "Sprite":
            case "Water":
            case "Fanta":
            case "Tea":
            case "Beer": {
                initBeveragesView();
                break;
            }

            case "Cake":
            case "Donut":
            case "Milkshake":
            case "Crepes":
            case "Ice cream":
            case "Pancake": {
                initDessertsView();
                break;
            }

            default: initMenuView();
        }
    }
}