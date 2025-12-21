package com.finpro.kel10.Frontend.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.finpro.kel10.Frontend.GameManager;
import com.finpro.kel10.Frontend.Main;
import com.finpro.kel10.Frontend.commands.Command;
import com.finpro.kel10.Frontend.commands.InputHandler;
import com.finpro.kel10.Frontend.enemies.BaseZombie;
import com.finpro.kel10.Frontend.enemies.BossZombie;
import com.finpro.kel10.Frontend.enemies.FinalBoss;
import com.finpro.kel10.Frontend.enemies.RunnerZombie;
import com.finpro.kel10.Frontend.entities.Bullet;
import com.finpro.kel10.Frontend.entities.Player;
import com.finpro.kel10.Frontend.factories.EnemyFactory;
import com.finpro.kel10.Frontend.factories.ItemFactory; // Import Baru
import com.finpro.kel10.Frontend.projectiles.BaseProjectile;
import com.finpro.kel10.Frontend.projectiles.HomingProjectile;
import com.finpro.kel10.Frontend.projectiles.SpitProjectile;
import com.finpro.kel10.Frontend.services.BulletPool;
import com.finpro.kel10.Frontend.strategies.*;
import com.finpro.kel10.Frontend.observers.ScoreUIObserver;

import java.util.ArrayList;
import java.util.HashMap; // Import Baru
import java.util.List;
import java.util.Map;     // Import Baru

public class PlayingState extends GameState {
    private Player player;
    private BulletPool bulletPool;
    private List<Bullet> activeBullets;
    private InputHandler inputHandler;

    private EnemyFactory enemyFactory;
    private List<BaseZombie> activeEnemies;
    private List<BaseProjectile> activeEnemyProjectiles;
    private ItemFactory itemFactory;

    private DifficultyStrategy currentStrategy;
    private Texture mapTexture;
    private GameManager gameManager;
    private float spawnTimer = 0;
    private int lastLoggedScore = -1;
    private ScoreUIObserver scoreUI;
    private boolean isTransitioning = false;

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
        scoreUI = new ScoreUIObserver();
        mapTexture = new Texture("map.png");

        // Setup Enemy
        enemyFactory = new EnemyFactory();
        activeEnemies = new ArrayList<>();
        activeEnemyProjectiles = new ArrayList<>();
        currentStrategy = new WaveOne();
        enemyFactory.setWeights(currentStrategy.getEnemyWeights());

        // Setup Items
        itemFactory = new ItemFactory();
        Map<String, Integer> itemWeights = new HashMap<>();
        // spawn probability (baru ada medkit)
        itemWeights.put("Medkit", 100);
        itemFactory.setWeights(itemWeights);

        player.addObserver(scoreUI);
    }

    private void spawnEnemy(float dt) {
        spawnTimer += dt;
        if (spawnTimer >= currentStrategy.getSpawnInterval()) {
            spawnTimer = 0;
            float posX = 0;
            float posY = 0;
            float buffer = 50f; //spawn distance outside screen

            int side = MathUtils.random(0, 3); //randomize spawn location *up, down, left, right
            switch (side) {
                case 0:
                    posX = MathUtils.random(0, Main.WIDTH);
                    posY = Main.HEIGHT + buffer;
                    break;
                case 1:
                    posX = MathUtils.random(0, Main.WIDTH);
                    posY = -buffer;
                    break;
                case 2:
                    posX = -buffer;
                    ;
                    posY = MathUtils.random(0, Main.HEIGHT);
                    break;
                case 3:
                    posX = Main.WIDTH + buffer;
                    posY = MathUtils.random(0, Main.HEIGHT);
                    break;
            }
            BaseZombie enemy = enemyFactory.createRandomEnemy(posX, posY);
            if (enemy != null) {
                activeEnemies.add(enemy);
            }
        }
    }

    private void updateEnemies(float dt) {
        for (int i = activeEnemies.size() - 1; i >= 0; i--) {
            BaseZombie enemy = activeEnemies.get(i);
            enemy.update(dt, player);

            if (enemy instanceof RunnerZombie) {
                SpitProjectile spit = ((RunnerZombie) enemy).getSpit();

                if (spit != null) {
                    activeEnemyProjectiles.add(spit);
                }
            }

            if (enemy instanceof BossZombie) {
                List<SpitProjectile> bossShots = ((BossZombie) enemy).getProjectiles();

                if (bossShots != null) {
                    // masukin semua peluru boss ke list projectile
                    activeEnemyProjectiles.addAll(bossShots);
                }
            }

            if (enemy instanceof FinalBoss) {
                List<BaseProjectile> bossShots = ((FinalBoss) enemy).getProjectiles();
                if (bossShots != null) {
                    activeEnemyProjectiles.addAll(bossShots);
                }
            }

            if (!enemy.isActive()) {
                activeEnemies.remove(i);
                enemyFactory.release(enemy);
            }
        }
    }

    private void updateEnemyProjectiles(float dt) {
        for (int i = activeEnemyProjectiles.size() - 1; i >= 0; i--) {
            BaseProjectile s = activeEnemyProjectiles.get(i);

            s.update(dt);

            // Cek Kena Player
            if (s.isActive() && s.getCollider().overlaps(player.getCollider())) {
                player.takeDamage(s.getDamage());
                s.setActive(false);
            }

        }
    }

    private void checkCollisions() {
        // --- 1. PELURU PLAYER KENA ZOMBIE ---
        for (Bullet b : activeBullets) {
            if (!b.isActive()) continue;
            for (BaseZombie z : activeEnemies) {
                if (!z.isActive()) continue;
                if (b.getCollider().overlaps(z.getCollider())) {
                    z.takeDamage(10);
                    b.setActive(false);
                    break;
                }
            }
        }

        // --- 2. PELURU PLAYER KENA PROJECTILE MUSUH (Homing Missile) ---
        for (Bullet b : activeBullets) {
            if (!b.isActive()) continue;

            // PERBAIKAN: Gunakan BaseProjectile di dalam loop, bukan SpitProjectile
            for (BaseProjectile enemyProj : activeEnemyProjectiles) {
                if (!enemyProj.isActive()) continue;

                // Cek apakah projectile ini tipe HomingProjectile (yang bisa ditembak)
                if (enemyProj instanceof HomingProjectile) {
                    if (b.getCollider().overlaps(enemyProj.getCollider())) {

                        // Hancurkan Missile
                        ((HomingProjectile) enemyProj).hitByPlayerBullet();

                        // Hancurkan Peluru Player
                        b.setActive(false);

                        break; // Lanjut ke peluru player berikutnya
                    }
                }
            }
        }

        // --- 3. ZOMBIE NABRAK PLAYER ---
        for (BaseZombie z : activeEnemies) {
            if (z.isActive()) {
                if (z.getCollider().overlaps(player.getCollider())) {
                    player.takeDamage(z.getDamage());
                }
            }
        }
    }

    private void updateDifficulty() {
        int score = gameManager.getScore();
        scoreUI.updateScore(score);

        if (score >= 250) {
            if (!(currentStrategy instanceof WaveThree) && !isTransitioning) {
                isTransitioning = true;

                gsm.push(new DifficultyTransitionState(gsm, this, new WaveThree(), "WAVE 3 INCOMING! (HARD)"));
            }
        }
        else if (score >= 150) {
            if (!(currentStrategy instanceof WaveTwo) && !isTransitioning) {
                isTransitioning = true;

                gsm.push(new DifficultyTransitionState(gsm, this, new WaveTwo(), "WAVE 2 INCOMING! (MEDIUM)"));
            }
        }
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.push(new PauseState(gsm));
            return;
        }
        float dt = Gdx.graphics.getDeltaTime();
        player.stopMoving();
        List<Command> commands = inputHandler.handleInput();
        for (Command command : commands) {
            command.execute(player, dt);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (player.canShoot()) {
                Bullet b = bulletPool.obtain();
                Vector2 gunPos = player.getGunTipPosition();
                b.init(gunPos.x, gunPos.y, player.getRotation());
                activeBullets.add(b);

                player.shoot();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt, cam);

        // Update Bullets
        for (int i = activeBullets.size() - 1; i >= 0; i--) {
            Bullet b = activeBullets.get(i);
            b.update(dt);
            if (!b.isActive()) {
                activeBullets.remove(i);
                bulletPool.release(b);
            }
        }

        spawnEnemy(dt);
        updateEnemies(dt);
        updateEnemyProjectiles(dt);
        scoreUI.updateScore(gameManager.getScore());

        checkCollisions();
        logScore();
        updateDifficulty();

        // Game Over Reset
        if (player.getCurrentHealth() <= 0) {
            System.out.println("GAME OVER");

            enemyFactory.releaseAllEnemies();
            itemFactory.releaseAllItems();
            activeEnemies.clear();
            activeBullets.clear();
            activeEnemyProjectiles.clear();

            gsm.set(new GameOverState(gsm));
        }

        itemFactory.update(dt, player);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        float mapWorldWidth = 1280f;
        float ratio = (float) mapTexture.getHeight() / mapTexture.getWidth();
        float mapWorldHeight = mapWorldWidth * ratio;

        sb.draw(mapTexture, 0, 0, mapWorldWidth, mapWorldHeight);
        itemFactory.render(sb);
        player.render(sb);

        for(Bullet b : activeBullets){
            if(b.isActive()) b.render(sb);
        }

        for(BaseZombie z : activeEnemies){
            if(z.isActive()) z.render(sb);
        }

        // PERBAIKAN: Loop menggunakan BaseProjectile
        for (BaseProjectile s : activeEnemyProjectiles) {
            if (s.isActive()) {
                s.render(sb);
            }
        }

        sb.end();

        scoreUI.render();
    }

    @Override
    public void dispose() {
        player.dispose();
        enemyFactory.releaseAllEnemies();
        activeEnemies.clear();
        player.removeObserver(scoreUI);

        // --- DISPOSE ITEMS ---
        itemFactory.releaseAllItems();
        scoreUI.dispose();
        mapTexture.dispose();
    }

    public void setStrategy(DifficultyStrategy strategy){
        this.currentStrategy = strategy;
        enemyFactory.setWeights(strategy.getEnemyWeights());
        this.isTransitioning = false;

        System.out.println("Strategy updated to: " + strategy.getClass().getSimpleName());
    }
}
