package projekjavafix.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameController {
    // Game state
    private int score;
    private int lives;
    private int round;
    private final int maxLives = 3;
    
    // Game data
    private Player impostor;
    private List<Player> players;
    private List<String> remainingClues;
    private Random random;
    private boolean gameStarted;
    
    public GameController() {
        this.random = new Random();
        initializePlayers();
        resetGame();
    }

    // Inner class Player
    public static class Player {
        private String name;
        private String role;
        private List<String> clues;

        public Player(String name, String role, List<String> clues) {
            this.name = name;
            this.role = role;
            this.clues = clues;
        }

        public String getName() { return name; }
        public String getRole() { return role; }
        public List<String> getClues() { return clues; }
    }

    private void initializePlayers() {
        players = new ArrayList<>();
        
        players.add(new Player("Player 1", "Dokter", Arrays.asList(
            "Tidur paling larut",
            "Paling sering ketemu klien",
            "Tidak pernah lihat sawah"
        )));
        
        players.add(new Player("Player 2", "Guru", Arrays.asList(
            "Banyak bicara di depan umum",
            "Pekerjaan paling berantakan", 
            "Sering bertemu anak-anak"
        )));
        
        players.add(new Player("Player 3", "Programmer", Arrays.asList(
            "Sering di luar ruangan",
            "Paling butuh listrik",
            "Selalu kerja pagi"
        )));
        
        players.add(new Player("Player 4", "Petani", Arrays.asList(
            "Tidak butuh koneksi internet",
            "Paling jarang duduk",
            "Bisa kerja dari rumah"
        )));
        
        players.add(new Player("Player 5", "Pengangguran", Arrays.asList(
            "Pekerja paling santai",
            "Paling sedikit interaksi sosial",
            "Bukan pekerja formal"
        )));
    }
    
    public void resetGame() {
        this.score = 0;
        this.lives = maxLives;
        this.round = 1;
        this.gameStarted = true;
        Collections.shuffle(players);
        this.impostor = players.get(0);
        this.remainingClues = new ArrayList<>(impostor.getClues());
        shufflePlayersAndSelectImpostor();
    }

    private void shufflePlayersAndSelectImpostor() {
    Collections.shuffle(players);
    this.impostor = players.get(random.nextInt(players.size())); // Pilih random, bukan selalu index 0
    this.remainingClues = new ArrayList<>(impostor.getClues());
    }

    
    public String getNextClue() {
        if (remainingClues.isEmpty() || impostor == null) {
            return "Tidak ada petunjuk lagi";
        }
        return remainingClues.remove(0);
    }

    public GuessResult processGuess(String guessedPlayer) {
        if (!gameStarted) {
            // Automatically reset game if someone tries to play when game is over
            resetGame();
            return new GuessResult(false, false, "Game telah direset! Silakan tebak lagi.");
        }
        
        if (guessedPlayer == null || guessedPlayer.isEmpty()) {
            return new GuessResult(false, false, "Pilih pemain terlebih dahulu!");
        }
        
        boolean isCorrect = guessedPlayer.equals(impostor.getName());
        
        if (isCorrect) {
            score += 100;
            round++;
            selectNewImpostor();
            return new GuessResult(true, false, "Benar! +100 poin. Ronde berikutnya!");
        } else {
            lives = Math.max(0, lives - 1);
            
            if (isGameOver()) {
                gameStarted = false;
                return new GuessResult(false, true, 
                    "Salah! Game Over. Skor akhir: " + score + ".");
            }
            
            return new GuessResult(false, false, 
                "Salah! Nyawa tersisa: " + lives);
        }
    }
    
    private void selectNewImpostor() {
        Collections.shuffle(players);
        this.impostor = players.get(random.nextInt(players.size()));
        this.remainingClues = new ArrayList<>(impostor.getClues());
        Collections.shuffle(remainingClues);
    }
    
    public boolean isGameOver() {
        return lives <= 0;
    }

    // Getter methods
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    public String getCurrentClue() {
        return remainingClues.isEmpty() ? "Tidak ada petunjuk" : remainingClues.get(0);
    }
    
    public int getScore() {
        return score;
    }
    
    public int getLives() {
        return lives;
    }
    
    public int getRound() {
        return round;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    public Player getImpostor() {
        return impostor;
    }
    
    public static class GuessResult {
        public final boolean isCorrect;
        public final boolean isGameOver;
        public final String message;
        
        public GuessResult(boolean isCorrect, boolean isGameOver, String message) {
            this.isCorrect = isCorrect;
            this.isGameOver = isGameOver;
            this.message = message;
        }
    }
}