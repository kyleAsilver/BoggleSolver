package dev.kylesilver.boggle.board;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import dev.kylesilver.boggle.io.FileReader;

public class Board {

    private Set<Tile> tiles;

    private Board() {
    }

    public Set<Tile> tiles() {
        return tiles;
    }

    public static class Builder {
        private Board board;

        public Builder(char[][] inputBoard) {
            board = new Board();
            board.tiles = parseBoard(inputBoard);
        }

        public Builder(Path path) {
            board = new Board();
            char[][] inputBoard = FileReader.getBoard(path.toString());
            board.tiles = parseBoard(inputBoard);
        }

        public Board build() {
            return board;
        }

        private Set<Tile> parseBoard(char[][] inputBoard) {
            Tile[][] tileGrid = toTileArray(inputBoard);
            updateNeighbors(tileGrid);
            return asSet(tileGrid);
        }

        private static Tile[][] toTileArray(char[][] inputBoard) {
            Tile[][] tileGrid = new Tile[inputBoard.length][inputBoard[0].length];
            for (int row = 0; row < inputBoard.length; row++) {
                for (int col = 0; col < inputBoard[row].length; col++) {
                    tileGrid[row][col] = new Tile(inputBoard[row][col], row, col);
                }
            }
            return tileGrid;
        }

        private static void updateNeighbors(Tile[][] tileGrid) {
            for (int row = 0; row < tileGrid.length; row++) {
                for (int col = 0; col < tileGrid[row].length; col++) {
                    populateNeighbors(tileGrid, row, col);
                }
            }
        }

        private static Set<Tile> asSet(Tile[][] tileGrid) {
            Set<Tile> tileSet = new HashSet<>();
            for (int row = 0; row < tileGrid.length; row++) {
                for (int col = 0; col < tileGrid[row].length; col++) {
                    tileSet.add(tileGrid[row][col]);
                }
            }
            return tileSet;
        }

        private static void populateNeighbors(Tile[][] tileGrid, int row, int col) {
            Tile target = tileGrid[row][col];

            addIfInBounds(tileGrid, target, row - 1, col - 1);
            addIfInBounds(tileGrid, target, row - 1, col + 1);
            addIfInBounds(tileGrid, target, row - 1, col);

            addIfInBounds(tileGrid, target, row, col - 1);
            addIfInBounds(tileGrid, target, row, col + 1);

            addIfInBounds(tileGrid, target, row + 1, col - 1);
            addIfInBounds(tileGrid, target, row + 1, col + 1);
            addIfInBounds(tileGrid, target, row + 1, col);
        }

        private static void addIfInBounds(Tile[][] tileGrid, Tile tile, int neighborRow, int neighborCol) {
            if (isInBounds(tileGrid, neighborRow, neighborCol)) {
                tile.addNeighbor(tileGrid[neighborRow][neighborCol]);
            }
        }

        private static boolean isInBounds(Tile[][] tileGrid, int row, int col) {
            if (row < 0 || row >= tileGrid.length) {
                return false;
            }
            if (col < 0 || col >= tileGrid[row].length) {
                return false;
            }
            return true;
        }
    }
}
