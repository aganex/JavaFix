package projekjavafix.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    // Game state
    private int score;
    private int lives;
    private int round;
    private final int maxLives = 3;
    
    // Game data
    private String impostor;
    private List<String> players;
    private Map<String, String> playerDescriptions;
    private List<String> clues;
    private String currentClue;
    
    // Utilities
    private Random random;
    private boolean gameStarted;
    
    public GameController() {
        this.random = new Random();
        initializeGameData();
        resetGame();
    }

    private void initializeGameData() {
        // Initialize players
        this.players = Arrays.asList(
            "Player 1", "Player 2", "Player 3", 
            "Player 4", "Player 5", "Player 6"
        );
        
        // Initialize player descriptions
        this.playerDescriptions = new HashMap<>();
        playerDescriptions.put("Player 1", "Sering di Electrical");
        playerDescriptions.put("Player 2", "Suka pakai warna merah");
        playerDescriptions.put("Player 3", "Diam-diam mencurigakan");
        playerDescriptions.put("Player 4", "Baru bergabung");
        playerDescriptions.put("Player 5", "Sering lapor");
        playerDescriptions.put("Player 6", "Tidak pernah lapor");
        
        // Initialize clues
        this.clues = Arrays.asList(
            "Biasa pakai warna merah",
            "Sering terlihat di medbay",
            "Tidak punya tugas di admin",
            "Berdiam diri di reactor",
            "Tidak pernah lapor dead body",
            "Sering ke electrical",
            "Terlihat mencurigakan di storage",
            "Pernah terlihat venting",
            "Tidak menyelesaikan tugas"
        );
    }
    
    public void resetGame() {
        this.score = 0;
        this.lives = maxLives;
        this.round = 1;
        this.gameStarted = true;
        selectNewImpostor();
    }
    
    public void selectNewImpostor() {
        if (players == null || players.isEmpty()) return;
        
        Collections.shuffle(players);
        this.impostor = players.get(random.nextInt(players.size()));
        this.currentClue = clues.get(random.nextInt(clues.size()));
    }
    
    public GuessResult processGuess(String guessedPlayer) {
        if (!gameStarted) {
            return new GuessResult(false, false, "Game belum dimulai!");
        }
        
        if (guessedPlayer == null || guessedPlayer.isEmpty()) {
            return new GuessResult(false, false, "Pilih pemain terlebih dahulu!");
        }
        
        boolean isCorrect = guessedPlayer.equals(impostor);
        
        if (isCorrect) {
            score += 100;
            round++;
            selectNewImpostor();
            return new GuessResult(true, false, "Benar! +100 poin");
        } else {
            lives = Math.max(0, lives - 1); // Ensure lives doesn't go negative
            
            if (isGameOver()) {
                gameStarted = false;
                return new GuessResult(false, true, 
                    "Salah! Game Over. Skor akhir: " + score);
            }
            
            return new GuessResult(false, false, 
                "Salah! Nyawa tersisa: " + lives);
        }
    }
    
    public boolean isGameOver() {
        return lives <= 0;
    }
    
    // Getter methods
    public List<String> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    public Map<String, String> getPlayerDescriptions() {
        return Collections.unmodifiableMap(playerDescriptions);
    }
    
    public String getPlayerDescription(String player) {
        return playerDescriptions.getOrDefault(player, "Pemain biasa");
    }
    
    public String getCurrentClue() {
        return currentClue;
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