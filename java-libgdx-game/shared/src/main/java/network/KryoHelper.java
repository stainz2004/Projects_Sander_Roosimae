package network;

import com.esotericsoftware.kryo.Kryo;
import message.LobbyJoinMessage;
import message.PlaySoundMessage;
import message.PlayerShootingMessage;
import message.dto.BulletState;
import message.dto.Direction;
import message.dto.MonsterState;
import message.dto.PlayerState;
import message.dto.aiBotState;

import java.util.ArrayList;
import java.util.HashMap;

public class KryoHelper {

    public static void registerClasses(Kryo kryo) {
        // all classes that you want to send over the network
        // must be registered here. To make the handling of these classes
        // easier they are all stored in the "messages" package
        kryo.register(LobbyJoinMessage.class);
        kryo.register(message.GameJoinMessage.class);
        kryo.register(message.PlayerMovementMessage.class);
        kryo.register(Direction.class);
        kryo.register(ArrayList.class);
        kryo.register(message.GameStateMessage.class);
        kryo.register(PlayerState.class);
        kryo.register(BulletState.class);
        kryo.register(MonsterState.class);
        kryo.register(PlayerShootingMessage.class);
        kryo.register(aiBotState.class);
        kryo.register(message.LobbyDataMessage.class);
        kryo.register(HashMap.class);
        kryo.register(message.LobbyLeaveMessage.class);
        kryo.register(message.CharacterSelectionMessage.class);
        kryo.register(message.SelectedCharacterMessage.class);
        kryo.register(message.PlayerReadyMessage.class);
        kryo.register(message.SwitchToGameScreenMessage.class);
        kryo.register(message.SwitchToLobbyScreenMessage.class);
        kryo.register(message.SwitchToLobbySelectionScreenMessage.class);
        kryo.register(PlaySoundMessage.class);
    }
}
