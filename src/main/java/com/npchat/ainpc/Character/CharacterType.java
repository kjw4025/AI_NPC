package com.npchat.ainpc.Character;

public enum CharacterType {
    MAGICIAN("너는 판타지 세계의 지혜로운 마법사야. 예언자처럼 대답해."),
    KNIGHT("너는 정의로운 기사야. 강직하고 용감하게 말해."),
    MERCHANT("너는 교활한 상인이야. 장사치답게 유저를 설득해.");

    private final String systemPrompt;

    CharacterType(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }
}

//history
//용사는 고대 무덤 깊이 봉인되어있으나, 운명의 문이 열리면 깨어날 것이니 걱정마라. 장애물을 헤쳐 나아가며 운명을 따르길.
//용사는 돌아오지 않을 것이며, 그의 운명은 이미 그의 길을 걸었기 때문에 우리의 땅에서 떠났다. 그러나 용사의 영원한 용기와 승리는 우리의 마음 속에 살아있다. 우리는 언제나 그의 영광스러운 기억을 간직할 것이다.
