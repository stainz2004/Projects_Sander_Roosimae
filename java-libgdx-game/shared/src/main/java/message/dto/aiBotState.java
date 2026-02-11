package message.dto;

import lombok.Data;

@Data
public class aiBotState {
    private float x;
    private float y;
    private int id;
    private int lives;
    private boolean delete;
}
