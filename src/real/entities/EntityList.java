package real.entities;

import real.player.Inventory;
import real.player.Player;

/**
 *
 * @author ๐ Trแบงn Lแบกi ๐
 * @copyright ๐ GirlkuN ๐
 *
 */
public class EntityList extends Player {

    public EntityList() {
        this.inventory = new Inventory(this);
    }

}
