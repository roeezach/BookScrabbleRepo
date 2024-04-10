package test;

import java.util.Arrays;
import java.util.Random;

public class Tile 
{
    public final int score;
    public final char letter;

    private Tile(int score, char letter) {
        this.score = score;
        this.letter = letter;
    }

    public int getScore() {
        return score;
    }
    public char getLetter() {
        return letter;
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + score;
        result = prime * result + letter;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (score != other.score)
            return false;
        if (letter != other.letter)
            return false;
        return true;
    }



    public static class Bag{
        private static final int MAX_TILE_QUANTITY = 98;
        private static Bag bagSingleInstance;
        static int[]  lettersQuantity;
        static Tile[] lettersTiles;

        private Bag() {
         lettersQuantity = new int[] { 9 , 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1 , 6 , 4, 6, 4, 2, 2, 1, 2, 1};
        lettersTiles = new Tile[] { 
            new Tile(1, 'A'), 
            new Tile(3, 'B'),
            new Tile(3, 'C'),
            new Tile(2, 'D'),
            new Tile(1, 'E'),
            new Tile(4, 'F'),
            new Tile(2, 'G'),            
            new Tile(4, 'H'),
            new Tile(1, 'I'),
            new Tile(8, 'J'),
            new Tile(5, 'K'),
            new Tile(1, 'L'),
            new Tile(3, 'M'),
            new Tile(1, 'N'),
            new Tile(1, 'O'),
            new Tile(3, 'P'),
            new Tile(10, 'Q'),
            new Tile(1, 'R'),
            new Tile(1, 'S'),
            new Tile(1, 'T'),
            new Tile(1, 'U'),
            new Tile(4, 'V'),
            new Tile(4, 'W'),
            new Tile(8, 'X'),
            new Tile(4, 'Y'),
            new Tile(10, 'Z')
            };
        }

        public static Bag getBag(){
            if( bagSingleInstance == null) bagSingleInstance = new Bag();
            return bagSingleInstance;
        }

        public static Tile getRand(){            
           if(isBagEmpty()) return null;
            Random randomInstance = new Random();
            Integer randomLetterLocation = randomInstance.nextInt(26);
            
            while (lettersQuantity[randomLetterLocation] == 0) {
                randomLetterLocation = randomInstance.nextInt(26);
            }
            lettersQuantity[randomLetterLocation]--;
            return lettersTiles[randomLetterLocation];
        }

        public static Tile getTile(char letter){
            int relativeIndex = getRelativeIndex(letter);      
            if (relativeIndex < 0 || relativeIndex > 25) return null;
            if(lettersQuantity[relativeIndex] == 0) return null;
            lettersQuantity[relativeIndex] --;
            return lettersTiles[relativeIndex];
        }

        public static int size(){
            int size = 0;
            for (int quantity : lettersQuantity) {
                size += quantity;
            }
            return size;
        }

        public static void put(Tile tileToPut){
            if(size() + 1 > MAX_TILE_QUANTITY) return;
            int index = getRelativeIndex(tileToPut.letter);
            lettersQuantity[index]++;
        }
        
        public static int[] getQuantities(){
            return Arrays.copyOf(lettersQuantity, lettersQuantity.length);
        }

        private static boolean isBagEmpty()
        {
            boolean allZero = true;
            for (int quantity : lettersQuantity) {
                if (quantity != 0) {
                    allZero = false;
                    break;
                }
            }
            return allZero;        
        }

        private static int getRelativeIndex(char letter) {
            int relativeIndex = letter - 'A';
            return relativeIndex;
        }
    }   
}
