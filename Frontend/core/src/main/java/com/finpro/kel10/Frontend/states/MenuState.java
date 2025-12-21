package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.finpro.kel10.Frontend.AudioManager;
import com.finpro.kel10.Frontend.GameManager;

public class MenuState extends GameState{
    private Stage stage;
    private TextField nameField;
    private Skin skin;
    private TextButton startButton;
    private AudioManager audioManager;
    private Texture backgroundTexture;

    public MenuState(GameStateManager gsm, AudioManager audioManager){
        super(gsm);
        this.audioManager = audioManager;
        this.audioManager.playBackgroundMusic();

        backgroundTexture = new Texture(Gdx.files.internal("BackgroundLead4Dead.png"));

        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        createBasicSkin();
        buildUI();
    }

    private void createBasicSkin(){
        skin = new Skin();
        BitmapFont defaultFont = new BitmapFont();
        skin.add("default", defaultFont);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        skin.add("gray", new Texture(pixmap));

        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fill();
        skin.add("dark_gray", new Texture(pixmap));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = skin.newDrawable("dark_gray");
        textFieldStyle.cursor = skin.newDrawable("white");
        textFieldStyle.selection = skin.newDrawable("gray");
        skin.add("default", textFieldStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = skin.newDrawable("gray");
        textButtonStyle.down = skin.newDrawable("white");
        textButtonStyle.over = skin.newDrawable("dark_gray");
        textButtonStyle.downFontColor = Color.BLACK;
        skin.add("default", textButtonStyle);
    }

    private void buildUI(){
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label label2 = new Label("Enter Your Name: ", skin);

        nameField = new TextField("", skin);

        nameField.setMessageText("Username...");
        nameField.setAlignment(Align.center);

        startButton = new TextButton("START GAME", skin);

        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent input, float x, float y){
                String name = nameField.getText();
                if(name == null || name.trim().isEmpty()){
                    name = "Survivor";
                }
                GameManager.getInstance().setUsername(name);
                GameManager.getInstance().resetScore();
                gsm.set(new PlayingState(gsm, audioManager));
            }
        });

        table.add(label2).padTop(150).padBottom(10);
        table.row();

        table.add(nameField).width(300).height(40).padBottom(20);
        table.row();

        table.add(startButton).width(200).height(50);
    }

    @Override
    public void handleInput(){}

    @Override
    public void update(float dt){
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch sb){
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(backgroundTexture, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.end();
        stage.draw();
    }

    @Override
    public void dispose(){
        if(stage!= null) stage.dispose();
        if(stage!= null) skin.dispose();
        if(backgroundTexture != null) backgroundTexture.dispose();
    }
}
