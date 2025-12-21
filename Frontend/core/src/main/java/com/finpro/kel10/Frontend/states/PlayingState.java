package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.AudioManager;
import com.finpro.kel10.Frontend.GameManager;
import com.finpro.kel10.Frontend.Main;
import com.finpro.kel10.Frontend.commands.Command;
import com.finpro.kel10.Frontend.commands.InputHandler;
import com.finpro.kel10.Frontend.enemies.BaseZombie;
import com.finpro.kel10.Frontend.entities.Bullet;
import com.finpro.kel10.Frontend.entities.Player;
import com.finpro.kel10.Frontend.factories.EnemyFactory;
import com.finpro.kel10.Frontend.services.BulletPool;
import com.finpro.kel10.Frontend.strategies.DifficultyStrategy;
import com.finpro.kel10.Frontend.strategies.WaveOne;
import com.finpro.kel10.Frontend.strategies.WaveThree;
import com.finpro.kel10.Frontend.strategies.WaveTwo;

import java.util.ArrayList;
import java.util.List;

public class PlayingState extends GameState {
    private Player player;
    private BulletPool bulletPool;
    private List<Bullet> activeBullets;
    private InputHandler inputHandler;
    private EnemyFactory enemyFactory;
    private List<BaseZombie> activeEnemies;
    private DifficultyStrategy currentStrategy;
    private GameManager gameManager;
    private float spawnTimer = 0;
    private int lastLoggedScore = -1;
    private AudioManager audioManager;

    private void logScore() {
        int currentScore = gameManager.getScore();
        if (currentScore > lastLoggedScore) {
            System.out.println("Current Score: " + currentScore);
            lastLoggedScore = currentScore;
        }
    }

    public PlayingState(GameStateManager gsm) {
        super(gsm);
        player = new Player(400, 300);
        bulletPool = new BulletPool();
        activeBullets = new ArrayList<>();
        inputHandler = new InputHandler();
        gameManager = GameManager.getInstance();
        enemyFactory = new EnemyFactory();
        activeEnemies = new ArrayList<>();
        currentStrategy = new WaveOne();
        enemyFactory.setWeights(currentStrategy.getEnemyWeights());
        audioManager = new AudioManager();

    }

    private void spawnEnemy(float dt){
        spawnTimer += dt;
        if(spawnTimer >= currentStrategy.getSpawnInterval()){
            spawnTimer = 0;
            float posX = 0;
            float posY = 0;
            float buffer = 50f; //spawn distance outside screen

            int side = MathUtils.random(0, 3); //randomize spawn location *up, down, left, right
            switch(side){
                case 0:
                    posX = MathUtils.random(0, Main.WIDTH);
                    posY = Main.HEIGHT+buffer;
                    break;
                case 1:
                    posX = MathUtils.random(0, Main.WIDTH);
                    posY = -buffer;
                    break;
                case 2:
                    posX = -buffer;;
                    posY = MathUtils.random(0, Main.HEIGHT);
                    break;
                case 3:
                    posX = Main.WIDTH+buffer;
                    posY = MathUtils.random(0, Main.HEIGHT);
                    break;
            }
            BaseZombie enemy = enemyFactory.createRandomEnemy(posX, posY);
            if(enemy!= null){
                activeEnemies.add(enemy);
            }
        }
    }

    private void updateEnemies(float dt){
        for(int i = activeEnemies.size()-1; i>=0; i--){
            BaseZombie enemy = activeEnemies.get(i);
            enemy.update(dt, player);
            if(!enemy.isActive()){
                activeEnemies.remove(i);
                enemyFactory.release(enemy);
            }
        }
    }

    private void checkCollisions(){
        for(Bullet b : activeBullets){
            if(!b.isActive()) continue;
            for(BaseZombie z : activeEnemies){
                if(!z.isActive()) continue;
                if(b.getCollider().overlaps(z.getCollider())){
                    z.takeDamage(10);
                    b.setActive(false);
                    break;
                }
            }
        }
        for(BaseZombie z : activeEnemies){
            if(z.isActive()){
                if(z.getCollider().overlaps(player.getCollider())){
                    player.takeDamage(z.getDamage());
                }
            }
        }
    }

    private void updateDifficulty(){
        int score = gameManager.getScore();
        if(score >= 100 && !(currentStrategy instanceof WaveThree)){ //for demo purpose
            currentStrategy = new WaveThree();
            enemyFactory.setWeights(currentStrategy.getEnemyWeights());
            System.out.println(">>> WAVE 3 STARTED! (Difficulty: HARD) <<<");
        }
        if(score >= 50 && score < 2000 && !(currentStrategy instanceof  WaveTwo)){ //for demo purpose
            currentStrategy = new WaveTwo();
            enemyFactory.setWeights((currentStrategy.getEnemyWeights()));
            System.out.println(">>> WAVE 2 STARTED! (Difficulty: MEDIUM) <<<");
        }
    }

    @Override
    public void handleInput() {
        // Cek jika tombol kiri mouse ditekan (JustClicked agar tidak nembak beruntun super cepat)
        float dt = Gdx.graphics.getDeltaTime();
        player.stopMoving();
        List<Command> commands = inputHandler.handleInput();
        for(Command command : commands){
            command.execute(player, dt);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Bullet b = bulletPool.obtain();
            Vector2 gunPos = player.getGunTipPosition();
            b.init(gunPos.x, gunPos.y, player.getRotation());
            activeBullets.add(b);
            // muzzle flash
            player.shoot();
            audioManager.playGunshotSFX();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt, cam);
        for(int i = activeBullets.size() - 1; i>= 0; i--) {
            Bullet b = activeBullets.get(i);
            b.update(dt);
            if (!b.isActive()) {
                activeBullets.remove(i);
                bulletPool.release(b);
            }
        }
        spawnEnemy(dt);
        updateEnemies(dt);
        checkCollisions();
        logScore();
        updateDifficulty();

        if(player.getCurrentHealth() <= 0){
            player.reset();
            enemyFactory.releaseAllEnemies();
            activeEnemies.clear();
            gameManager.resetScore();
            currentStrategy = new WaveOne();
            enemyFactory.setWeights(currentStrategy.getEnemyWeights());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        player.render(sb);
        for(Bullet b : activeBullets){
            b.render(sb); // Render peluru
        }

        for(BaseZombie z : activeEnemies){
            z.render(sb);
        }

        sb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
        enemyFactory.releaseAllEnemies();
        activeEnemies.clear();
    }
}
