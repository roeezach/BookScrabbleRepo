package test;

import java.util.Arrays;

public class Word {
    ///row, col define the location of the first tile.
    // vertical - top to bottom is vertial true else false
    Tile[] tiles;
    boolean isVertical;
    boolean isContainUnderscore = false;
    int row,col;
    
    public Word(Tile[] word, int row, int col, boolean vertical) {
        this.tiles = word;
        this.row = row;
        this.col = col;
        this.isVertical = vertical;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getIsVertical() {
        return isVertical;
    }
        
    public boolean getIsContainUnderscore() {
        return isContainUnderscore;
    }
    
    public void setIsContainUnderscore(Boolean isContainUnderscore) {
        this.isContainUnderscore = isContainUnderscore;
    }

    public boolean hasUnderscore(Word word) {
        for (Tile tile : word.getTiles()) {
            if (tile == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Word other = (Word) obj;
        if (!Arrays.equals(tiles, other.tiles))
            return false;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        if (isVertical != other.isVertical)
            return false;
        return true;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public void setVertical(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public void setContainUnderscore(boolean isContainUnderscore) {
        this.isContainUnderscore = isContainUnderscore;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
    
}
