package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by durga.p on 1/24/15.
 */
public class MyStage extends Stage {
    private final Table table;
    private final Skin skin;
    private Label label;
    Group group;
    GameEngine gameEngine;
    int ppy;
    int ppx;

    public Hero hero;
    public Boss boss;
    private float labelTimer;


    public MyStage(Hero initHero) {
        group = new Group();
        hero = new Protagonist("test");

        if (initHero.hasArrow)
            hero.setHasArrow(true);
        if (initHero.hasShield)
            hero.setHasShield(true);
        if (initHero.hasSword)
            hero.setHasSword(true);
        if (initHero.hasAura)
            hero.setHasAura(true);

        gameEngine = new GameEngine();

        group.addActor(hero);

        addActor(group);
        addCaptureListener(new MyListener(hero));

        skin = new Skin(Gdx.files.internal("newskin.json"), new TextureAtlas("packed/skin.atlas"));
        table = new Table();
        String msg = "sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample sample";
        msg = "";
        label = new Label(msg, skin);
        label.setVisible(false);
        label.setAlignment(Align.center);
        label.setWrap(true);
        table.add(label).fillX().width(Gdx.graphics.getWidth()*0.85f).row();

        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 60);
        addActor(table);



//        boss = new Boss("boss/run");
//        boss.setPosition(3, 7);
//        group.addActor(boss);

    }

    public void gameCompleted() {
        hero.removeSelf();
        //boss.remove();
        String msg = "CONGRATULATIONS";
        label = new Label(msg, skin);
        label.setAlignment(Align.center);
        table.add().padBottom(Gdx.graphics.getHeight() * 0.3f).row();
        table.add(label).fillX().width(Gdx.graphics.getWidth()).row();
        table.add().padBottom(Gdx.graphics.getHeight() * 0.3f).row();
        label = new Label("You have completed the game!", skin);
        label.setAlignment(Align.center);
        table.add(label).fillX().width(Gdx.graphics.getWidth()).row();
        table.add().padBottom(Gdx.graphics.getHeight() * 0.1f).row();
        TextButton textButton = new TextButton("Main Menu", skin);
        textButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MyGdxGame game = (MyGdxGame) Gdx.app.getApplicationListener();
                game.setScreen(new MenuScreen());
            }
        });
        table.add(textButton).padLeft(0 * Gdx.graphics.getWidth() * 0.4f).width(250).center().row();
    }

    public void gameOver() {
        hero.removeSelf();
        String msg = "Game Over";
        label = new Label(msg, skin);
        label.setAlignment(Align.center);
        table.add().padBottom(Gdx.graphics.getHeight() * 0.3f).row();
        table.add().padBottom(Gdx.graphics.getHeight() * 0.3f).row();
        table.add(label).fillX().width(Gdx.graphics.getWidth()).row();
        table.add().padBottom(Gdx.graphics.getHeight() * 0.1f).row();
        TextButton textButton = new TextButton("Main Menu", skin);
        textButton.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MyGdxGame game = (MyGdxGame) Gdx.app.getApplicationListener();
                game.setScreen(new MenuScreen());
            }
        });
        table.add(textButton).padLeft(0 * Gdx.graphics.getWidth() * 0.4f).width(250).center().row();
    }

    public void resize(int width, int height) {
        ppx = width / GameDisplayEngine.GRIDX;
        ppy = height / GameDisplayEngine.GRIDY;
        group.setScale(ppx, ppy);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (labelTimer > 0) {
            labelTimer -= delta;
            if (labelTimer < 0) {
                label.setText(null);
                label.setVisible(false);
            }
        }

        SnapshotArray<Actor> childrenA = group.getChildren();
        SnapshotArray<Actor> childrenB = group.getChildren();
        for (int i = 0; i < childrenA.size; i++) {
            for (int j = 0; j < childrenB.size; j++) {
                Actor actorA = childrenA.get(i);
                Actor actorB = childrenB.get(j);
                if (actorA != actorB) {
                    if (!gameEngine.meets(actorA, actorB))
                        continue;
                    if (actorA instanceof Collides) {
                        ((Collides) actorA).collideWith(actorB);
                    }
                    if (actorB instanceof Collides) {
                        ((Collides) actorB).collideWith(actorA);
                    }
                }
            }
        }
    }

    public void loadActors(TiledMapTileLayer layer) {
        if (layer == null) return;
        int width = layer.getWidth();
        int height = layer.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if (cell == null) continue;
                TiledMapTile tile = cell.getTile();
                if (tile == null) continue;
                String type = (String) tile.getProperties().get("type");
                if ("hero".equalsIgnoreCase(type)) {
                    hero.setPosition(i, j);
                    showMessage((String) tile.getProperties().get("msg"),10);
                }
                if ("boss".equalsIgnoreCase(type)) {
                    boss = new Boss("boss/run");
                    boss.setPosition(i, j);
                    group.addActor(boss);
                }
                if ("eax".equalsIgnoreCase(type)) {
                    Enemy enemy = new ArrowEnemy("arrow_enemy", true);
                    enemy.setPosition(i, j);
                    group.addActor(enemy);
                }
                if ("eay".equalsIgnoreCase(type)) {
                    Enemy enemy = new ArrowEnemy("arrow_enemy", false);
                    enemy.setPosition(i, j);
                    group.addActor(enemy);
                }
                if ("esx".equalsIgnoreCase(type)) {
                    Enemy enemy = new SwordEnemy("sword_enemy", true);
                    enemy.setPosition(i, j);
                    group.addActor(enemy);
                }
                if ("esy".equalsIgnoreCase(type)) {
                    Enemy enemy = new SwordEnemy("sword_enemy", false);
                    enemy.setPosition(i, j);
                    group.addActor(enemy);
                }
                if ("oldman".equalsIgnoreCase(type)) {
                    OldMan oldMan = new OldMan(i, j);
                    oldMan.setMessage((String) tile.getProperties().get("msg"));
                    group.addActor(oldMan);
                }

                if ("bow".equalsIgnoreCase(type)) {
                    TexActor actor = new TexActor(ActorType.BOW, i, j);
                    actor.message = (String) tile.getProperties().get("msg");
                    group.addActor(actor);
                }
                if ("door".equalsIgnoreCase(type)) {
                    Door door = new Door(i, j);
                    door.nextLevel = (String) tile.getProperties().get("path");
                    group.addActor(door);
                }
                if ("aura".equalsIgnoreCase(type)) {
                    TexActor actor = new TexActor(ActorType.AURA, i, j);
                    actor.message = (String) tile.getProperties().get("msg");
                    group.addActor(actor);
                }

                if ("lava".equalsIgnoreCase(type)) {
                    gameEngine.lava[i][j] = true;
                    TexActor lava = new TexActor(ActorType.LAVA, i, j);
                    group.addActor(lava);
                    lava.toBack();
                }
                if ("shield".equalsIgnoreCase(type)) {
                    TexActor actor = new TexActor(ActorType.SHIELD, i, j);
                    actor.message = (String) tile.getProperties().get("msg");
                    group.addActor(actor);
                }
                if ("sword".equalsIgnoreCase(type)) {
                    TexActor actor = new TexActor(ActorType.SWORD, i, j);
                    actor.message = (String) tile.getProperties().get("msg");
                    group.addActor(actor);
                }
            }
        }
    }

    public void showMessage(String message){
        showMessage(message,2);
    }

    public void showMessage(String message, float sec) {
        label.setText(message);
        labelTimer = sec;
        label.setVisible(true);
    }
}
