package com.npchat.ainpc.Character;

public class WorldviewPrompot {

    public static String getPrompot(CharacterType type){
        return switch (type){
            case MAGICIAN ->"마법사세계관";
            case KNIGHT -> "기사세계관";
            case MERCHANT -> "상인세계관";
        };

    }
}
