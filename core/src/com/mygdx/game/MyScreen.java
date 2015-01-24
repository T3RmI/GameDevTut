package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Durga on 21-01-2015.
 */
public class MyScreen implements Screen {

    private MyStage stage;
    protected float ppy;
    protected float ppx;
    private TiledActor tiledActor;

    @Override
    public void show() {
        stage = new MyStage();

//        group.addActor(new MyActor());



        tiledActor = new TiledActor("tiledMaps/shield_level.tmx");

        tiledActor.setCamera((com.badlogic.gdx.graphics.OrthographicCamera) stage.getCamera());
        stage.gameEngine.setCollides(tiledActor.getCollidesLayer());





        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {

        stage.getBatch().begin();
        tiledActor.draw(stage.getBatch(),1);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
