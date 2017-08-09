package com.example.moposnsplatdemo.rank;

public class RankItem {

    public RankItem(final int rank, final String nickname, final long score,final boolean isSelf) {
        this.rank = rank;
        this.nickname = nickname;
        this.score = score;
        this.isSelf = isSelf;
    }

    int rank;
    String nickname;
    long score;
    boolean isSelf;

    public int getRank() {
        return rank;
    }

    public String getNickname() {
        return nickname;
    }

    public long getScore() {
        return score;
    }
    
    public boolean getIsSelf() {
        return isSelf;
    }
}
