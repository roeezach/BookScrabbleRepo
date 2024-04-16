package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private static Board boardSingleInstance;
    private static final int boardSize = 15;
    private Tile[][] gameBoard;
    private Map<Coordinate, Color> specialSquaresWord;
    private Map<Coordinate, Color> specialSquaresLetter;
    
    private Board() {
        gameBoard = new Tile[boardSize][boardSize];        
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = null;
            }
        }
        BuildSpecialSquares();
    }

    public void BuildSpecialSquares()
    {
        specialSquaresWord = new HashMap<>();
        specialSquaresLetter = new HashMap<>();  
        specialSquaresWord.put(new Coordinate(7, 7), Color.STAR);
        BuildRedSquares();
        BuildPaleBlueSquares();
        BuildBlueSquares();
        BuildYellowSquares();
    }

    public static Board getBoard(){
        if (boardSingleInstance == null) 
            boardSingleInstance =  new Board();
        return boardSingleInstance;
    }

    public Tile[][] getTiles(){
        return Arrays.copyOf(gameBoard, gameBoard.length);
    }

    public boolean boardLegal(Word givenWord){
        int col = givenWord.getCol();
        int row = givenWord.getRow();

        if (givenWord.getTiles().length < 1) 
            return false;
        if(!isWordOnBoarder(givenWord))
            return false;
        if(gameBoard[7][7] == null)
            return isWordOnStarSquare(givenWord);

        return isTileUsingAnotherTile(givenWord) && isWordChangingAnotherWord(givenWord, row, col); 
    }

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> createdWords = new ArrayList<>();
        int row = word.getRow();
        int col = word.getCol();
        boolean isPartialWordCreated = false;
    
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.getTiles()[i] != null) {
                if (word.getIsVertical()) {
                    if (isVerticalPartialWordStart(row, col, i, word) || isVerticalPartialWordEnd(row, col, i, word)) {
                        AddPartialCreatedWord(word, createdWords, row, col, true, i);
                        isPartialWordCreated = true;                        
                    }
                    if (hasHorizontalNeighbor(row, col)) 
                        createdWords.add(createPartialWord(row, col, word, i, false));                    
                }                 
                else {
                    if (isHorizontalPartialWordStart(row, col, i, word) || isHorizontalPartialWordEnd(row, col, i, word)) {
                        AddPartialCreatedWord(word, createdWords, row, col, false, i);
                        if (!isPartialWordCreated) 
                            createdWords.add(createPartialWord(row, col, word, i, false));
                        isPartialWordCreated = true;                        
                    }
                    if (hasVerticalNeighbor(row, col)) 
                        createdWords.add(createPartialWord(row, col, word, i, true));                    
                }
            }
            if(word.getIsVertical())
                row++;
            else
                col++;                
        }
        AddFullWord(word, createdWords, isPartialWordCreated);        
        return createdWords;
    }

    private void AddFullWord(Word word, ArrayList<Word> createdWords, boolean isPartialWordCreated) {
        if (!isPartialWordCreated) 
            createdWords.add(fullWord(word));
    }

    private void AddPartialCreatedWord(Word word, ArrayList<Word> createdWords, int row, int col, boolean isPartialWordCreated,int i) {
        if (!isPartialWordCreated) 
            createdWords.add(createPartialWord(row, col, word, i, true));
    }
    
    private boolean isVerticalPartialWordStart(int row, int col, int index, Word word) {
        return index == 0 && row > 0 && gameBoard[row - 1][col] != null;
    }
    
    private boolean isVerticalPartialWordEnd(int row, int col, int index, Word word) {
        return index == word.getTiles().length - 1 && row < 14 && gameBoard[row + 1][col] != null;
    }
    
    private boolean hasHorizontalNeighbor(int row, int col) {
        return col > 0 && gameBoard[row][col - 1] != null || col < 14 && gameBoard[row][col + 1] != null;
    }
    
    private boolean isHorizontalPartialWordStart(int row, int col, int index, Word word) {
        return index == 0 && col > 0 && gameBoard[row][col - 1] != null;
    }
    
    private boolean isHorizontalPartialWordEnd(int row, int col, int index, Word word) {
        return index == word.getTiles().length - 1 && col < 14 && gameBoard[row][col + 1] != null;
    }
    
    private boolean hasVerticalNeighbor(int row, int col) {
        return row > 0 && gameBoard[row - 1][col] != null || row < 14 && gameBoard[row + 1][col] != null;
    }
    
    private Word createPartialWord(int row, int col, Word word, int index, boolean isVertical) {
        return getWord(row, col, word, index, isVertical);
    }
    
    public int getScore(Word word){
        
        int sum=0;
        int multiplier=1;
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;
        boolean isWordMultiplied = false;
        for(int i = 0; i < len; i++){
            Coordinate currentCoordinate = new Coordinate(row, col);
            if(specialSquaresWord.containsKey(currentCoordinate))
            {
                multiplier = getMultiplierForColor( specialSquaresWord.get(currentCoordinate));
                if(word.getTiles()[i] != null)
                    sum += word.getTiles()[i].getScore();
                isWordMultiplied = true;
            }
            else if(specialSquaresLetter.containsKey(currentCoordinate)){
                multiplier = getMultiplierForColor(specialSquaresLetter.get(currentCoordinate));
                if(word.getTiles()[i] != null)                
                    sum += word.getTiles()[i].getScore() * multiplier;
            }
            else    
                sum += word.getTiles()[i].getScore();
        
            if(word.getIsVertical())
                row++;
            else
                col++;
        }
            if(isWordMultiplied)
                return sum * multiplier;
            else return sum;    
    }

    public int tryPlaceWord(Word word){
        int sum = 0;
        ArrayList<Word> createdWords = new ArrayList<Word>();
        
        if(boardLegal(word)){
            createdWords = getWords(word);
            for (Word w : createdWords) {            
                sum += getScore(w);
           }
        }
        else return 0;
        addWordToBoard(word);
        return sum;
    }
    
    private void addWordToBoard(Word word){
        int row = word.getRow();
        int col = word.getCol();        
        for (int i = 0; i < word.getTiles().length; i++) {
            if(gameBoard[row][col] == null)
                gameBoard[row][col] = word.getTiles()[i];
            if(word.getIsVertical()) row++;
            else col++;            
        }
    }

    private int getMultiplierForColor(Color color) {
        if(color == Color.STAR){
            specialSquaresWord.remove(new Coordinate(7, 7));
            return 2;
        }        
        if(color == Color.PALE_BLUE) 
            return 2;
        if(color == Color.BLUE) 
            return 3;
        if(color == Color.YELLOW) 
            return 2;
        if(color == Color.RED)
            return 3;
        else
            return 1;
    }

    public boolean dictionaryLegal() {
        // check existance of word in dictionary, stage 1 always true
        return true;
    }
            
    private Word getWord(int row, int col, Word word, int i, boolean isVertical) {
        int startRow = row;
        int startCol = col;
        int endRow = row;
        int endCol = col;
        int index = i;
    
        if (isVertical) {
            if (word.getIsVertical()) {
                index = 0;
                startRow = word.getRow();
                endRow = word.getRow() + word.getTiles().length - 1;
            }
            startRow = findStartRow(startRow, col);
            endRow = findEndRow(endRow, col);
            Tile[] tiles = getTilesForRowRange(startRow, endRow, col, index, word);
            return new Word(tiles, startRow, col, isVertical);
        } else {
            if (!word.getIsVertical()) {
                index = 0;
                startCol = word.getCol();
                endCol = word.getCol() + word.getTiles().length - 1;
            }
            startCol = findStartColumn(row, startCol);
            endCol = findEndColumn(row, endCol);
            Tile[] tiles = getTilesForColumnRange(row, startCol, endCol, index, word);
            return new Word(tiles, row, startCol, isVertical);
        }
    }
    
    private int findStartRow(int startRow, int col) {
        while (startRow > 0 && gameBoard[startRow - 1][col] != null) {
            startRow--;
        }
        return startRow;
    }
    
    private int findEndRow(int endRow, int col) {
        while (endRow < 14 && gameBoard[endRow + 1][col] != null) {
            endRow++;
        }
        return endRow;
    }
    
    private Tile[] getTilesForRowRange(int startRow, int endRow, int col, int index, Word word) {
        Tile[] tiles = new Tile[endRow - startRow + 1];
        for (int j = 0; j < tiles.length; j++) {
            if (gameBoard[startRow + j][col] == null) {
                tiles[j] = getTileFromWordOrBoard(word, index);
                index = getNextTileIndex(word, index);
            } else {
                tiles[j] = gameBoard[startRow + j][col];
            }
        }
        return tiles;
    }
    
    private int findStartColumn(int row, int startCol) {
        while (startCol > 0 && gameBoard[row][startCol - 1] != null) {
            startCol--;
        }
        return startCol;
    }
    
    private int findEndColumn(int row, int endCol) {
        while (endCol < 14 && gameBoard[row][endCol + 1] != null) {
            endCol++;
        }
        return endCol;
    }
    
    private Tile[] getTilesForColumnRange(int row, int startCol, int endCol, int index, Word word) {
        Tile[] tiles = new Tile[endCol - startCol + 1];
        for (int j = 0; j < tiles.length; j++) {
            if (gameBoard[row][startCol + j] == null) {
                tiles[j] = getTileFromWordOrBoard(word, index);
                index = getNextTileIndex(word, index);
            } else {
                tiles[j] = gameBoard[row][startCol + j];
            }
        }
        return tiles;
    }
    
    private Tile getTileFromWordOrBoard(Word word, int index) {
        if (index >= 0 && index < word.getTiles().length) {
            Tile tile = word.getTiles()[index];
            if (tile != null) {
                return tile;
            }
        }
        return null;
    }
    
    private int getNextTileIndex(Word word, int index) {
        index++;
        while (index < word.getTiles().length - 1 && word.getTiles()[index] == null) {
            index++;
        }
        return index;
    }
                
    private Word fullWord(Word word) {
        int col = word.getCol();
        int row = word.getRow();
        Tile[] fullWordTiles = new Tile[word.getTiles().length];
        populateFullWordTiles(word, row, col, fullWordTiles);
        return new Word(fullWordTiles, row, col, word.getIsVertical());
    }
    
    private void populateFullWordTiles(Word word, int row, int col, Tile[] fullWordTiles) {
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.getTiles()[i] != null) 
                fullWordTiles[i] = word.getTiles()[i];
            else {
                if (word.getIsVertical())
                    fullWordTiles[i] = gameBoard[row + i][col];
                 else 
                    fullWordTiles[i] = gameBoard[row][col + i];                
            }
        }
    }
    
    private boolean isWordChangingAnotherWord(Word givenWord, int startRow, int startCol){
        int wordLength = givenWord.getTiles().length;
        if(givenWord.getIsVertical()){
            for (int i = 0; i < wordLength; i++) {
                if(gameBoard[startRow + i][startCol] != null && givenWord.getTiles()[i] != null) 
                    return false;
                if(gameBoard[startRow + i][startCol] == null && givenWord.getTiles()[i] == null) 
                    return false;
            }           
        }
        else {
            for (int i = 0; i < wordLength ; i++) {
                if(gameBoard[startRow][startCol + i] != null && givenWord.getTiles()[i] != null)
                    return false;                    
                if(gameBoard[startRow][startCol + i] == null && givenWord.getTiles()[i] == null)
                    return false;
            }
        }
        return true;        
    }

    private boolean isTileUsingAnotherTile(Word word)
    {
        int row = word.getRow();
        int col = word.getCol();
        for (Tile[] tiles : gameBoard) {
            if (row - 1 >= 0 && gameBoard[row - 1][col] != null)
                return true;
            if (row + 1 >= 0 && gameBoard[row + 1][col] != null)
                return true;
            if (col - 1 >= 0 && gameBoard[row][col - 1] != null)
                return true;
            if (col + 1 >= 0 && gameBoard[row][col + 1] != null)
                return true;

            if (word.getIsVertical()) {
                row += 1;
            } else {
                col += 1;
            }
        }
        return false;
    }

    private boolean isWordOnStarSquare(Word word) {
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;;

        if (word.getIsVertical()) {
            if (col != 7 || row > 7 || row + len - 1 < 7) {
                return false;
            }
        } else {
            if (row != 7 || col > 7 || col + len - 1 < 7) {
                return false;
            }
        }
        return true;
    }

    private boolean isWordOnBoarder(Word word) {
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;;

        if (col > 14 || col < 0 || row > 14 || row < 0)
            return false;
        if(word.getIsVertical() && len > boardSize - row)
            return false;
        if(!word.getIsVertical() && len > boardSize - col)
            return false;
        return true;
    }

    private void BuildBlueSquares() {
        specialSquaresLetter.put(new Coordinate(1, 5), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(1, 9), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(5, 1), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(5, 5), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(5, 9), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(5, 13), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(9, 1), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(9, 5), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(9, 9), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(9, 13), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(13, 5), Color.BLUE);
        specialSquaresLetter.put(new Coordinate(13, 9), Color.BLUE);
    }

    private void BuildPaleBlueSquares() {
        specialSquaresLetter.put(new Coordinate(0, 3), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(0, 11), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(2, 6), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(2, 8), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(3, 0), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(3, 7), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(3, 14), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(6, 2), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(6, 6), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(6, 8), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(6, 12), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(7, 3), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(7, 11), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(8, 2), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(8, 6), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(8, 8), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(8, 12), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(11, 0), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(11, 7), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(11, 14), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(12, 6), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(12, 8), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(14, 3), Color.PALE_BLUE);
        specialSquaresLetter.put(new Coordinate(14, 11), Color.PALE_BLUE);
    }

    private void BuildRedSquares() {
        specialSquaresWord.put(new Coordinate(0, 0), Color.RED);
        specialSquaresWord.put(new Coordinate(0, 7), Color.RED);
        specialSquaresWord.put(new Coordinate(0, 14), Color.RED);
        specialSquaresWord.put(new Coordinate(7, 0), Color.RED);
        specialSquaresWord.put(new Coordinate(7, 14), Color.RED);
        specialSquaresWord.put(new Coordinate(14, 0), Color.RED);
        specialSquaresWord.put(new Coordinate(14, 7), Color.RED);
        specialSquaresWord.put(new Coordinate(14, 14), Color.RED);
    }

    private void BuildYellowSquares() {
        specialSquaresWord.put(new Coordinate(1, 1), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(1, 13), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(2, 2), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(2, 12), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(3, 3), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(3, 11), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(4, 4), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(4, 10), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(10, 4), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(10,10 ), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(11, 3), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(11, 11), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(12, 2), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(12, 12), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(13, 1), Color.YELLOW);
        specialSquaresWord.put(new Coordinate(13, 13), Color.YELLOW);
    }

    public static class Coordinate{
        private int row;
        private int col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + row;
            result = prime * result + col;
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
            Coordinate other = (Coordinate) obj;
            if (row != other.row)
                return false;
            if (col != other.col)
                return false;
            return true;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
        
    }

    enum Color {
        STAR,
        PALE_BLUE,
        BLUE,
        YELLOW,
        RED,
        GREEN
      }
}
