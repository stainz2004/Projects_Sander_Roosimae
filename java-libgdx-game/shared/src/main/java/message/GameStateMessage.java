package message;

import lombok.Data;
import message.dto.BulletState;
import message.dto.PlayerState;
import message.dto.MonsterState;
import message.dto.aiBotState;

import java.util.List;

@Data
public class GameStateMessage {
    private List<PlayerState> playerStates;
    private List<BulletState> bulletStates;
    private MonsterState monsterState;
    private List<aiBotState> aiBotStates;
    private int gameTime;
    private boolean allPlayersHaveJoined;
}
