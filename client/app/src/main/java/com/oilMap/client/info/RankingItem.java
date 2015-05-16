package com.oilMap.client.info;

/**
 * Created by 김현준 on 2015-05-16.
 */
public class RankingItem {

    public int getIcon() {
        return icon;
    }

    public String getId() {
        return Id;
    }

    public int getRankingIcon() {
        return rankingIcon;
    }

    public String getEfficiency() {
        return efficiency;
    }

    private String key = "여기에 ID키값 넣을 것";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    private String efficiency;
    private int icon;
    private String Id;
    private int rankingIcon;

    public RankingItem(String Id){
        this.Id = Id;
    }

    public RankingItem(int icon, String Id, int rankingIcon, String efficiency){
        this.icon = icon;
        this.Id = Id;
        this.rankingIcon = rankingIcon;
        this.efficiency = efficiency;
    }
}
