package message;

import java.util.List;

public class SelectedCharacterMessage {
    private List<Integer> selectedCharacterIds;

    // Kryo requires a zero‐arg constructor
    public SelectedCharacterMessage() {}

    public SelectedCharacterMessage(List<Integer> selectedCharacterIds) {
        this.selectedCharacterIds = selectedCharacterIds;
    }

    public List<Integer> getSelectedCharacterIds() {
        return selectedCharacterIds;
    }
}
