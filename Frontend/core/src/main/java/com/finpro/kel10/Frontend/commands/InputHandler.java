package com.finpro.kel10.Frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private List<Command> commands = new ArrayList<>();

    public List<Command> handleInput(){
        commands.clear();
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            commands.add(new MoveUpCommand());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            commands.add(new MoveDownCommand());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            commands.add(new MoveRightCommand());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            commands.add(new MoveLeftCommand());
        }
        return commands;
    }
}
