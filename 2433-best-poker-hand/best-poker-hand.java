    class Solution {
    public String bestHand(int[] ranks, char[] suits) {
        HashMap<Integer, Integer> map = new HashMap<>(); 
        boolean flush = true;
        for(int i = 1; i < suits.length; i++){
            if(suits[0] != suits[i]){
                flush = false;
                break;
            }
        }
        if(flush) return "Flush"; 
        for(int i = 0; i < ranks.length; i++){
            map.put(ranks[i], map.getOrDefault(ranks[i], 0) + 1);
        }
        boolean pair = false;
        for(int rank : map.keySet()){
            if(map.get(rank) >= 3){
                return "Three of a Kind";
            }
            if(map.get(rank) == 2){
                pair = true;
            }
        }
        if(pair) return "Pair";
        return "High Card";
    }
}